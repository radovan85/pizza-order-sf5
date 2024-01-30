package com.radovan.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.radovan.spring.dto.CartDto;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.dto.PizzaDto;
import com.radovan.spring.dto.PizzaSizeDto;
import com.radovan.spring.dto.ShippingAddressDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.OrderService;
import com.radovan.spring.service.PizzaService;
import com.radovan.spring.service.PizzaSizeService;
import com.radovan.spring.service.ShippingAddressService;
import com.radovan.spring.service.UserService;

@Controller
@RequestMapping(value = "/orders")
public class OrderController {

	@Autowired
	private CartService cartService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ShippingAddressService shippingAddressService;

	@Autowired
	private UserService userService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private PizzaService pizzaService;

	@Autowired
	private PizzaSizeService pizzaSizeService;

	@Autowired
	private OrderService orderService;

	@GetMapping(value = "/confirmUserData")
	public String confirmData(ModelMap map) {
		UserDto authUser = userService.getCurrentUser();
		CustomerDto customer = customerService.getCustomerByUserId(authUser.getId());
		ShippingAddressDto shippingAddress = new ShippingAddressDto();
		ShippingAddressDto currentAddress = shippingAddressService.getShippingAddress(customer.getShippingAddressId());
		map.put("shippingAddress", shippingAddress);
		map.put("currentAddress", currentAddress);
		return "fragments/userDataConfirm :: ajaxLoadedContent";
	}

	@PostMapping(value = "/confirmShippingAddress")
	public String confirmShippingAddress(@ModelAttribute("shippingAddress") ShippingAddressDto shippingAddress) {
		shippingAddressService.addShippingAddress(shippingAddress);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/phoneConfirmation")
	public String confirmPhone(ModelMap map) {
		CustomerDto customer = new CustomerDto();
		UserDto authUser = userService.getCurrentUser();
		CustomerDto currentCustomer = customerService.getCustomerByUserId(authUser.getId());
		map.put("customer", customer);
		map.put("currentCustomer", currentCustomer);
		return "fragments/phoneConfirmation :: ajaxLoadedContent";
	}

	@PostMapping(value = "/phoneConfirmation")
	public String confirmPhonePost(@ModelAttribute("customer") CustomerDto customer) {
		customerService.addCustomer(customer);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/orderConfirmation")
	public String renderOrderForm(ModelMap map) {
		OrderDto order = new OrderDto();
		UserDto authUser = userService.getCurrentUser();
		CustomerDto customer = customerService.getCustomerByUserId(authUser.getId());
		CartDto cart = cartService.getCartByCartId(customer.getCartId());
		List<CartItemDto> allCartItems = cartItemService.listAllByCartId(cart.getCartId());
		List<PizzaDto> allPizzas = pizzaService.listAll();
		List<PizzaSizeDto> allPizzaSizes = pizzaSizeService.listAll();
		ShippingAddressDto shippingAddress = shippingAddressService.getShippingAddress(customer.getShippingAddressId());
		map.put("order", order);
		map.put("allCartItems", allCartItems);
		map.put("allPizzas", allPizzas);
		map.put("allPizzaSizes", allPizzaSizes);
		map.put("shippingAddress", shippingAddress);
		map.put("cart", cart);
		return "fragments/orderConfirmation :: ajaxLoadedContent";
	}

	@GetMapping(value = "/cancelOrder")
	public String cancelOrder() {
		return "fragments/checkOutCancelled :: ajaxLoadedContent";
	}

	@GetMapping(value = "/createOrder")
	public String addOrder() {
		orderService.addCustomerOrder();
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/orderCompleted")
	public String orderExecuted() {
		return "fragments/orderCompleted :: ajaxLoadedContent";
	}

}
