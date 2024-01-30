package com.radovan.spring.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.dto.ShippingAddressDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.entity.CustomerEntity;
import com.radovan.spring.entity.RoleEntity;
import com.radovan.spring.entity.ShippingAddressEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.exceptions.ExistingEmailException;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.form.RegistrationForm;
import com.radovan.spring.repository.CartRepository;
import com.radovan.spring.repository.CustomerRepository;
import com.radovan.spring.repository.RoleRepository;
import com.radovan.spring.repository.ShippingAddressRepository;
import com.radovan.spring.repository.UserRepository;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.OrderService;
import com.radovan.spring.service.UserService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ShippingAddressRepository shippAddressRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@Autowired
	private OrderService orderService;

	@Override
	public List<CustomerDto> getAllCustomers() {
		// TODO Auto-generated method stub
		Optional<List<CustomerEntity>> allCustomersOpt = Optional.ofNullable(customerRepository.findAll());
		List<CustomerDto> returnValue = new ArrayList<CustomerDto>();
		if (!allCustomersOpt.isEmpty()) {
			allCustomersOpt.get().forEach((customer) -> {
				CustomerDto customerDto = tempConverter.customerEntityToDto(customer);
				returnValue.add(customerDto);
			});
		}

		return returnValue;
	}

	@Override
	public CustomerDto getCustomer(Integer id) {
		// TODO Auto-generated method stub
		Optional<CustomerEntity> customerOpt = customerRepository.findById(id);
		CustomerDto returnValue = new CustomerDto();
		if (customerOpt.isPresent()) {
			returnValue = tempConverter.customerEntityToDto(customerOpt.get());
		} else {
			Error error = new Error("Invalid Customer");
			throw new InstanceUndefinedException(error);
		}
		return returnValue;
	}

	@Override
	public CustomerDto getCustomerByUserId(Integer userId) {
		// TODO Auto-generated method stub

		Optional<CustomerEntity> customerOpt = Optional.ofNullable(customerRepository.findByUserId(userId));
		CustomerDto returnValue = new CustomerDto();
		if (customerOpt.isPresent()) {
			returnValue = tempConverter.customerEntityToDto(customerOpt.get());
		} else {
			Error error = new Error("Invalid Customer");
			throw new InstanceUndefinedException(error);
		}
		return returnValue;
	}

	@Override
	public CustomerDto storeCustomer(RegistrationForm form) {
		// TODO Auto-generated method stub
		UserDto userDto = form.getUser();
		Optional<UserEntity> testUser = Optional.ofNullable(userRepository.findByEmail(userDto.getEmail()));
		if (testUser.isPresent()) {
			Error error = new Error("Email exists");
			throw new ExistingEmailException(error);
		}

		RoleEntity role = roleRepository.findByRole("ROLE_USER");
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		userDto.setEnabled((byte) 1);
		List<RoleEntity> roles = new ArrayList<RoleEntity>();
		roles.add(role);
		UserEntity userEntity = tempConverter.userDtoToEntity(userDto);
		userEntity.setRoles(roles);
		userEntity.setEnabled((byte) 1);
		UserEntity storedUser = userRepository.save(userEntity);
		List<UserEntity> users = role.getUsers();
		if (users == null) {
			users = new ArrayList<>();
		}

		users.add(storedUser);
		role.setUsers(users);
		roleRepository.saveAndFlush(role);

		ShippingAddressDto shippAddressDto = form.getShippingAddress();
		ShippingAddressEntity shippAddressEntity = tempConverter.shippingAddressDtoToEntity(shippAddressDto);
		ShippingAddressEntity storedShippAddress = shippAddressRepository.save(shippAddressEntity);

		CartEntity cartEntity = new CartEntity();
		cartEntity.setCartPrice(0f);
		CartEntity storedCart = cartRepository.save(cartEntity);

		CustomerDto customerDto = form.getCustomer();
		customerDto.setUserId(storedUser.getId());
		customerDto.setCartId(storedCart.getCartId());
		customerDto.setShippingAddressId(storedShippAddress.getShippingAddressId());
		CustomerEntity customerEntity = tempConverter.customerDtoToEntity(customerDto);
		CustomerEntity storedCustomer = customerRepository.save(customerEntity);

		storedCart.setCustomer(storedCustomer);
		cartRepository.saveAndFlush(storedCart);

		storedShippAddress.setCustomer(storedCustomer);
		shippAddressRepository.saveAndFlush(storedShippAddress);

		CustomerDto returnValue = tempConverter.customerEntityToDto(storedCustomer);
		return returnValue;
	}

	@Override
	public CustomerDto addCustomer(CustomerDto customer) {
		// TODO Auto-generated method stub

		CustomerEntity customerEntity = tempConverter.customerDtoToEntity(customer);
		CustomerEntity storedCustomer = customerRepository.save(customerEntity);
		CustomerDto returnValue = tempConverter.customerEntityToDto(storedCustomer);
		return returnValue;
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// TODO Auto-generated method stub
		CustomerDto customer = getCustomer(customerId);
		List<OrderDto> allOrders = orderService.listAllByCartId(customer.getCartId());
		if (!allOrders.isEmpty()) {
			allOrders.forEach((order) -> {
				orderService.deleteOrder(order.getOrderId());
			});
		}
		customerRepository.deleteById(customerId);
		customerRepository.flush();
	}

	@Override
	public CustomerDto getCurrentCustomer() {
		// TODO Auto-generated method stub
		CustomerDto returnValue = null;
		UserDto authUser = userService.getCurrentUser();
		returnValue = getCustomerByUserId(authUser.getId());
		return returnValue;
	}

}
