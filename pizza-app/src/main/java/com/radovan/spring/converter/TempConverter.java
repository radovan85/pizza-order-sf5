package com.radovan.spring.converter;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.radovan.spring.dto.CartDto;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.OrderAddressDto;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.dto.OrderItemDto;
import com.radovan.spring.dto.PizzaDto;
import com.radovan.spring.dto.PizzaSizeDto;
import com.radovan.spring.dto.RoleDto;
import com.radovan.spring.dto.ShippingAddressDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.entity.CartItemEntity;
import com.radovan.spring.entity.CustomerEntity;
import com.radovan.spring.entity.OrderAddressEntity;
import com.radovan.spring.entity.OrderEntity;
import com.radovan.spring.entity.OrderItemEntity;
import com.radovan.spring.entity.PizzaEntity;
import com.radovan.spring.entity.PizzaSizeEntity;
import com.radovan.spring.entity.RoleEntity;
import com.radovan.spring.entity.ShippingAddressEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.repository.CartRepository;
import com.radovan.spring.repository.CustomerRepository;
import com.radovan.spring.repository.OrderAddressRepository;
import com.radovan.spring.repository.OrderItemRepository;
import com.radovan.spring.repository.OrderRepository;
import com.radovan.spring.repository.PizzaRepository;
import com.radovan.spring.repository.PizzaSizeRepository;
import com.radovan.spring.repository.RoleRepository;
import com.radovan.spring.repository.ShippingAddressRepository;
import com.radovan.spring.repository.UserRepository;

@Component
public class TempConverter {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ShippingAddressRepository shippingAddressRepository;

	@Autowired
	private PizzaRepository pizzaRepository;

	@Autowired
	private PizzaSizeRepository pizzaSizeRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderAddressRepository orderAddressRepository;

	private DecimalFormat decfor = new DecimalFormat("0.00");

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	public CartDto cartEntityToDto(CartEntity cartEntity) {
		CartDto returnValue = mapper.map(cartEntity, CartDto.class);
		Optional<Float> cartPriceOpt = Optional.ofNullable(cartEntity.getCartPrice());
		if (!cartPriceOpt.isPresent()) {
			returnValue.setCartPrice(0f);
		}
		Optional<CustomerEntity> customerOpt = Optional.ofNullable(cartEntity.getCustomer());
		if (customerOpt.isPresent()) {
			returnValue.setCustomerId(customerOpt.get().getCustomerId());
		}

		List<Integer> itemsIds = new ArrayList<>();
		Optional<List<CartItemEntity>> cartItemsOpt = Optional.ofNullable(cartEntity.getCartItems());
		if (!cartItemsOpt.isEmpty()) {
			cartItemsOpt.get().forEach((itemEntity) -> {
				Integer itemId = itemEntity.getCartItemId();
				itemsIds.add(itemId);
			});
		}
		returnValue.setCartItemsIds(itemsIds);

		Float cartPrice = Float.valueOf(decfor.format(returnValue.getCartPrice()));
		returnValue.setCartPrice(cartPrice);
		return returnValue;

	}

	public CartEntity cartDtoToEntity(CartDto cartDto) {
		CartEntity returnValue = mapper.map(cartDto, CartEntity.class);
		Optional<Float> cartPriceOpt = Optional.ofNullable(cartDto.getCartPrice());
		if (!cartPriceOpt.isPresent()) {
			returnValue.setCartPrice(0f);
		}
		Optional<Integer> customerIdOpt = Optional.ofNullable(cartDto.getCustomerId());
		if (customerIdOpt.isPresent()) {
			Integer customerId = customerIdOpt.get();
			CustomerEntity customerEntity = customerRepository.findById(customerId).get();
			returnValue.setCustomer(customerEntity);
		}

		List<CartItemEntity> cartItems = new ArrayList<>();
		Optional<List<Integer>> itemIdsOpt = Optional.ofNullable(cartDto.getCartItemsIds());

		if (!itemIdsOpt.isEmpty()) {
			itemIdsOpt.get().forEach((itemId) -> {
				CartItemEntity itemEntity = cartItemRepository.findById(itemId).get();
				cartItems.add(itemEntity);
			});
		}
		returnValue.setCartItems(cartItems);
		Float cartPrice = Float.valueOf(decfor.format(returnValue.getCartPrice()));
		returnValue.setCartPrice(cartPrice);
		return returnValue;
	}

	public CartItemDto cartItemEntityToDto(CartItemEntity cartItemEntity) {
		CartItemDto returnValue = mapper.map(cartItemEntity, CartItemDto.class);

		Optional<PizzaSizeEntity> pizzaSizeOpt = Optional.ofNullable(cartItemEntity.getPizzaSize());
		if (pizzaSizeOpt.isPresent()) {
			Float price = pizzaSizeOpt.get().getPrice();
			Integer quantity = returnValue.getQuantity();
			price = price * quantity;
			price = Float.valueOf(decfor.format(price));
			returnValue.setPrice(price);
			returnValue.setPizzaSizeId(pizzaSizeOpt.get().getPizzaSizeId());
		}

		Optional<CartEntity> cartOpt = Optional.ofNullable(cartItemEntity.getCart());
		if (cartOpt.isPresent()) {
			returnValue.setCartId(cartOpt.get().getCartId());
		}

		return returnValue;
	}

	public CartItemEntity cartItemDtoToEntity(CartItemDto cartItemDto) {
		CartItemEntity returnValue = mapper.map(cartItemDto, CartItemEntity.class);
		Optional<Integer> cartIdOpt = Optional.ofNullable(cartItemDto.getCartId());
		if (cartIdOpt.isPresent()) {
			Integer cartId = cartIdOpt.get();
			CartEntity cartEntity = cartRepository.findById(cartId).get();
			returnValue.setCart(cartEntity);
		}

		Optional<Integer> pizzaSizeIdOpt = Optional.ofNullable(cartItemDto.getPizzaSizeId());
		if (pizzaSizeIdOpt.isPresent()) {
			Integer pizzaSizeId = pizzaSizeIdOpt.get();
			PizzaSizeEntity pizzaSizeEntity = pizzaSizeRepository.findById(pizzaSizeId).get();
			Float price = pizzaSizeEntity.getPrice();
			Integer quantity = returnValue.getQuantity();
			price = price * quantity;
			price = Float.valueOf(decfor.format(price));
			returnValue.setPrice(price);
			returnValue.setPizzaSize(pizzaSizeEntity);
		}

		return returnValue;
	}

	public CustomerDto customerEntityToDto(CustomerEntity customerEntity) {
		CustomerDto returnValue = mapper.map(customerEntity, CustomerDto.class);

		Optional<ShippingAddressEntity> shippingAddressOpt = Optional.ofNullable(customerEntity.getShippingAddress());
		if (shippingAddressOpt.isPresent()) {
			returnValue.setShippingAddressId(shippingAddressOpt.get().getShippingAddressId());
		}

		Optional<CartEntity> cartOpt = Optional.ofNullable(customerEntity.getCart());
		if (cartOpt.isPresent()) {
			returnValue.setCartId(cartOpt.get().getCartId());
		}

		Optional<UserEntity> userOpt = Optional.ofNullable(customerEntity.getUser());
		if (userOpt.isPresent()) {
			returnValue.setUserId(userOpt.get().getId());
		}

		return returnValue;
	}

	public CustomerEntity customerDtoToEntity(CustomerDto customerDto) {
		CustomerEntity returnValue = mapper.map(customerDto, CustomerEntity.class);

		Optional<Integer> shippingAddressIdOpt = Optional.ofNullable(customerDto.getShippingAddressId());
		if (shippingAddressIdOpt.isPresent()) {
			Integer shippingAddressId = shippingAddressIdOpt.get();
			ShippingAddressEntity shippingAddressEntity = shippingAddressRepository.findById(shippingAddressId).get();
			returnValue.setShippingAddress(shippingAddressEntity);
		}

		Optional<Integer> cartIdOpt = Optional.ofNullable(customerDto.getCartId());
		if (cartIdOpt.isPresent()) {
			Integer cartId = cartIdOpt.get();
			CartEntity cartEntity = cartRepository.findById(cartId).get();
			returnValue.setCart(cartEntity);
		}

		Optional<Integer> userIdOpt = Optional.ofNullable(customerDto.getUserId());
		if (userIdOpt.isPresent()) {
			Integer userId = userIdOpt.get();
			UserEntity userEntity = userRepository.findById(userId).get();
			returnValue.setUser(userEntity);
		}

		return returnValue;
	}

	public PizzaDto pizzaEntityToDto(PizzaEntity pizzaEntity) {
		PizzaDto returnValue = mapper.map(pizzaEntity, PizzaDto.class);

		Optional<List<PizzaSizeEntity>> pizzaSizesOpt = Optional.ofNullable(pizzaEntity.getPizzaSizes());
		List<Integer> pizzaSizesIds = new ArrayList<Integer>();
		if (!pizzaSizesOpt.isEmpty()) {
			pizzaSizesOpt.get().forEach((size) -> {
				pizzaSizesIds.add(size.getPizzaSizeId());
			});
		}

		returnValue.setPizzaSizesIds(pizzaSizesIds);
		return returnValue;
	}

	public PizzaEntity pizzaDtoToEntity(PizzaDto pizzaDto) {
		PizzaEntity returnValue = mapper.map(pizzaDto, PizzaEntity.class);

		Optional<List<Integer>> pizzaSizeIdsOpt = Optional.ofNullable(pizzaDto.getPizzaSizesIds());
		List<PizzaSizeEntity> pizzaSizes = new ArrayList<PizzaSizeEntity>();

		if (!pizzaSizeIdsOpt.isEmpty()) {
			pizzaSizeIdsOpt.get().forEach((sizeId) -> {
				PizzaSizeEntity pizzaSizeEntity = pizzaSizeRepository.findById(sizeId).get();
				pizzaSizes.add(pizzaSizeEntity);
			});
		}

		returnValue.setPizzaSizes(pizzaSizes);
		return returnValue;
	}

	public PizzaSizeDto pizzaSizeEntityToDto(PizzaSizeEntity sizeEntity) {
		PizzaSizeDto returnValue = mapper.map(sizeEntity, PizzaSizeDto.class);
		Optional<PizzaEntity> pizzaOpt = Optional.ofNullable(sizeEntity.getPizza());
		if (pizzaOpt.isPresent()) {
			returnValue.setPizzaId(pizzaOpt.get().getPizzaId());
		}

		Float price = Float.valueOf(decfor.format(returnValue.getPrice()));
		returnValue.setPrice(price);
		return returnValue;
	}

	public PizzaSizeEntity pizzaSizeDtoToEntity(PizzaSizeDto sizeDto) {
		PizzaSizeEntity returnValue = mapper.map(sizeDto, PizzaSizeEntity.class);
		Optional<Integer> pizzaIdOpt = Optional.ofNullable(sizeDto.getPizzaId());
		if (pizzaIdOpt.isPresent()) {
			Integer pizzaId = pizzaIdOpt.get();
			PizzaEntity pizzaEntity = pizzaRepository.findById(pizzaId).get();
			returnValue.setPizza(pizzaEntity);
		}

		Float price = Float.valueOf(decfor.format(returnValue.getPrice()));
		returnValue.setPrice(price);
		return returnValue;
	}

	public OrderDto orderEntityToDto(OrderEntity orderEntity) {
		OrderDto returnValue = mapper.map(orderEntity, OrderDto.class);

		Optional<OrderAddressEntity> addressOpt = Optional.ofNullable(orderEntity.getAddress());
		if (addressOpt.isPresent()) {
			returnValue.setAddressId(addressOpt.get().getOrderAddressId());
		}

		Optional<CartEntity> cartOpt = Optional.ofNullable(orderEntity.getCart());
		if (cartOpt.isPresent()) {
			returnValue.setCartId(cartOpt.get().getCartId());
		}

		List<Integer> orderedItemsIds = new ArrayList<>();
		Optional<List<OrderItemEntity>> orderedItemsOpt = Optional.ofNullable(orderEntity.getOrderedItems());
		if (!orderedItemsOpt.isEmpty()) {
			orderedItemsOpt.get().forEach((item) -> {
				Integer itemId = item.getOrderItemId();
				orderedItemsIds.add(itemId);
			});
		}

		Optional<Timestamp> createdAtOpt = Optional.ofNullable(orderEntity.getCreatedAt());
		if (createdAtOpt.isPresent()) {
			LocalDateTime createdAtLocal = createdAtOpt.get().toLocalDateTime();
			String createdAt = createdAtLocal.format(formatter);
			returnValue.setCreatedAt(createdAt);
		}

		returnValue.setOrderedItemsIds(orderedItemsIds);

		return returnValue;
	}

	public OrderEntity orderDtoToEntity(OrderDto orderDto) {
		OrderEntity returnValue = mapper.map(orderDto, OrderEntity.class);

		Optional<Integer> addressIdOpt = Optional.ofNullable(orderDto.getAddressId());
		if (addressIdOpt.isPresent()) {
			Integer addressId = addressIdOpt.get();
			OrderAddressEntity address = orderAddressRepository.findById(addressId).get();
			returnValue.setAddress(address);
		}

		Optional<Integer> cartIdOpt = Optional.ofNullable(orderDto.getCartId());
		if (cartIdOpt.isPresent()) {
			Integer cartId = cartIdOpt.get();
			CartEntity cartEntity = cartRepository.findById(cartId).get();
			returnValue.setCart(cartEntity);
		}

		List<OrderItemEntity> orderedItems = new ArrayList<>();
		Optional<List<Integer>> orderedItemsIdsOpt = Optional.ofNullable(orderDto.getOrderedItemsIds());
		if (!orderedItemsIdsOpt.isEmpty()) {
			orderedItemsIdsOpt.get().forEach((itemId) -> {
				OrderItemEntity itemEntity = orderItemRepository.findById(itemId).get();
				orderedItems.add(itemEntity);
			});
		}

		returnValue.setOrderedItems(orderedItems);
		return returnValue;
	}

	public OrderAddressDto orderAddressEntityToDto(OrderAddressEntity address) {
		OrderAddressDto returnValue = mapper.map(address, OrderAddressDto.class);
		Optional<OrderEntity> orderOpt = Optional.ofNullable(address.getOrder());
		if (orderOpt.isPresent()) {
			returnValue.setOrderId(orderOpt.get().getOrderId());
		}

		return returnValue;
	}

	public OrderAddressEntity orderAddressDtoToEntity(OrderAddressDto address) {
		OrderAddressEntity returnValue = mapper.map(address, OrderAddressEntity.class);
		Optional<Integer> orderIdOpt = Optional.ofNullable(address.getOrderId());
		if (orderIdOpt.isPresent()) {
			Integer orderId = orderIdOpt.get();
			OrderEntity orderEntity = orderRepository.findById(orderId).get();
			returnValue.setOrder(orderEntity);
		}

		return returnValue;
	}

	public OrderItemDto orderItemEntityToDto(OrderItemEntity itemEntity) {
		OrderItemDto returnValue = mapper.map(itemEntity, OrderItemDto.class);

		Optional<OrderEntity> orderOpt = Optional.ofNullable(itemEntity.getOrder());
		if (orderOpt.isPresent()) {
			returnValue.setOrderId(orderOpt.get().getOrderId());
		}

		return returnValue;
	}

	public OrderItemEntity orderItemDtoToEntity(OrderItemDto itemDto) {
		OrderItemEntity returnValue = mapper.map(itemDto, OrderItemEntity.class);

		Optional<Integer> orderIdOpt = Optional.ofNullable(itemDto.getOrderId());
		if (orderIdOpt.isPresent()) {
			Integer orderId = orderIdOpt.get();
			OrderEntity orderEntity = orderRepository.findById(orderId).get();
			returnValue.setOrder(orderEntity);
		}

		return returnValue;
	}

	public OrderItemDto cartItemToOrderItemDto(CartItemDto cartItem) {
		OrderItemDto returnValue = mapper.map(cartItem, OrderItemDto.class);

		Optional<PizzaSizeEntity> pizzaSizeOpt = pizzaSizeRepository.findById(cartItem.getPizzaSizeId());
		if (pizzaSizeOpt.isPresent()) {
			Optional<PizzaEntity> pizzaOpt = Optional.ofNullable(pizzaSizeOpt.get().getPizza());
			if (pizzaOpt.isPresent()) {
				returnValue.setPizza(pizzaOpt.get().getName());
				returnValue.setPizzaSize(pizzaSizeOpt.get().getName());
				returnValue.setPizzaPrice(pizzaSizeOpt.get().getPrice());
				returnValue.setPrice(pizzaSizeOpt.get().getPrice() * returnValue.getQuantity());
				Float price = Float.valueOf(decfor.format(returnValue.getPrice()));
				returnValue.setPrice(price);
			}
		}

		return returnValue;
	}

	public ShippingAddressDto shippingAddressEntityToDto(ShippingAddressEntity addressEntity) {
		ShippingAddressDto returnValue = mapper.map(addressEntity, ShippingAddressDto.class);

		Optional<CustomerEntity> customerOpt = Optional.ofNullable(addressEntity.getCustomer());
		if (customerOpt.isPresent()) {
			returnValue.setCustomerId(customerOpt.get().getCustomerId());
		}

		return returnValue;
	}

	public ShippingAddressEntity shippingAddressDtoToEntity(ShippingAddressDto addressDto) {
		ShippingAddressEntity returnValue = mapper.map(addressDto, ShippingAddressEntity.class);

		Optional<Integer> customerIdOpt = Optional.ofNullable(addressDto.getCustomerId());
		if (customerIdOpt.isPresent()) {
			Integer customerId = customerIdOpt.get();
			CustomerEntity customerEntity = customerRepository.findById(customerId).get();
			returnValue.setCustomer(customerEntity);
		}

		return returnValue;
	}

	public OrderAddressDto shippingAddressToOrderAddress(ShippingAddressDto shippingAddress) {
		// TODO Auto-generated method stub
		OrderAddressDto returnValue = mapper.map(shippingAddress, OrderAddressDto.class);
		return returnValue;
	}

	public UserDto userEntityToDto(UserEntity userEntity) {
		UserDto returnValue = mapper.map(userEntity, UserDto.class);
		returnValue.setEnabled(userEntity.getEnabled());
		Optional<List<RoleEntity>> rolesOpt = Optional.ofNullable(userEntity.getRoles());
		List<Integer> rolesIds = new ArrayList<Integer>();

		if (!rolesOpt.isEmpty()) {
			rolesOpt.get().forEach((roleEntity) -> {
				rolesIds.add(roleEntity.getId());
			});
		}

		returnValue.setRolesIds(rolesIds);

		return returnValue;
	}

	public UserEntity userDtoToEntity(UserDto userDto) {
		UserEntity returnValue = mapper.map(userDto, UserEntity.class);
		List<RoleEntity> roles = new ArrayList<>();
		Optional<List<Integer>> rolesIdsOpt = Optional.ofNullable(userDto.getRolesIds());

		if (!rolesIdsOpt.isEmpty()) {
			rolesIdsOpt.get().forEach((roleId) -> {
				RoleEntity role = roleRepository.findById(roleId).get();
				roles.add(role);
			});
		}

		returnValue.setRoles(roles);

		return returnValue;
	}

	public RoleDto roleEntityToDto(RoleEntity roleEntity) {
		RoleDto returnValue = mapper.map(roleEntity, RoleDto.class);
		Optional<List<UserEntity>> usersOptional = Optional.ofNullable(roleEntity.getUsers());
		List<Integer> userIds = new ArrayList<>();

		if (!usersOptional.isPresent()) {
			usersOptional.get().forEach((user) -> {
				userIds.add(user.getId());
			});
		}

		returnValue.setUsersIds(userIds);
		return returnValue;
	}

	public RoleEntity roleDtoToEntity(RoleDto roleDto) {
		RoleEntity returnValue = mapper.map(roleDto, RoleEntity.class);
		Optional<List<Integer>> usersIdsOptional = Optional.ofNullable(roleDto.getUsersIds());
		List<UserEntity> users = new ArrayList<>();

		if (!usersIdsOptional.isEmpty()) {
			usersIdsOptional.get().forEach((userId) -> {
				UserEntity userEntity = userRepository.findById(userId).get();
				users.add(userEntity);
			});
		}

		returnValue.setUsers(users);
		return returnValue;
	}
}
