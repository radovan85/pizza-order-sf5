package com.radovan.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.OrderAddressDto;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.dto.OrderItemDto;
import com.radovan.spring.dto.PizzaDto;
import com.radovan.spring.dto.PizzaSizeDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.OrderAddressService;
import com.radovan.spring.service.OrderItemService;
import com.radovan.spring.service.OrderService;
import com.radovan.spring.service.PizzaService;
import com.radovan.spring.service.PizzaSizeService;
import com.radovan.spring.service.UserService;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

	@Autowired
	private PizzaService pizzaService;

	@Autowired
	private PizzaSizeService pizzaSizeService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private UserService userService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderAddressService orderAddressService;

	@Autowired
	private OrderItemService orderItemService;

	@GetMapping(value = "/createPizza")
	public String renderPizzaForm(ModelMap map) {
		PizzaDto pizza = new PizzaDto();
		map.put("pizza", pizza);
		return "fragments/pizzaForm :: ajaxLoadedContent";
	}

	@GetMapping(value = "/allPizzas")
	public String listAllPizzas(ModelMap map) {
		List<PizzaDto> allPizzas = pizzaService.listAll();
		map.put("allPizzas", allPizzas);
		map.put("recordsPerPage", 5);
		return "fragments/pizzaList :: ajaxLoadedContent";
	}

	@GetMapping(value = "/invalidPath")
	public String invalidImagePath() {
		return "fragments/invalidImagePath :: ajaxLoadedContent";
	}

	@PostMapping(value = "/createPizza")
	public String createBook(@ModelAttribute("pizza") PizzaDto pizza) {

		pizzaService.addPizza(pizza);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/pizzaDetails/{pizzaId}")
	public String getPizzaDetails(@PathVariable("pizzaId") Integer pizzaId, ModelMap map) {
		PizzaDto pizza = pizzaService.getPizzaById(pizzaId);
		map.put("pizza", pizza);
		return "fragments/pizzaDetails :: ajaxLoadedContent";
	}

	@GetMapping(value = "/updatePizza/{pizzaId}")
	public String renderUpdatePizzaForm(@PathVariable("pizzaId") Integer pizzaId, ModelMap map) {
		PizzaDto pizza = new PizzaDto();
		PizzaDto currentPizza = pizzaService.getPizzaById(pizzaId);
		map.put("pizza", pizza);
		map.put("currentPizza", currentPizza);
		return "fragments/updatePizzaForm :: ajaxLoadedContent";
	}

	@GetMapping(value = "/deletePizza/{pizzaId}")
	public String deletePizza(@PathVariable("pizzaId") Integer pizzaId) {

		pizzaService.deletePizza(pizzaId);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/sizeList/{pizzaId}")
	public String getSizeList(@PathVariable("pizzaId") Integer pizzaId, ModelMap map) {
		List<PizzaSizeDto> allPizzaSizes = pizzaSizeService.listAllByPizzaId(pizzaId);
		map.put("allPizzaSizes", allPizzaSizes);
		return "fragments/pizzaSizesForPizza :: ajaxLoadedContent";
	}

	@GetMapping(value = "/allSizes")
	public String getAllPizzaSizes(ModelMap map) {
		List<PizzaSizeDto> allPizzaSizes = pizzaSizeService.listAll();
		List<PizzaDto> allPizzas = pizzaService.listAll();
		map.put("allPizzaSizes", allPizzaSizes);
		map.put("allPizzas", allPizzas);
		map.put("recordsPerPage", 5);
		return "fragments/pizzaSizeList :: ajaxLoadedContent";
	}

	@GetMapping(value = "/allSizes/{pizzaId}")
	public String allSizesByPizzaId(@PathVariable("pizzaId") Integer pizzaId, ModelMap map) {
		List<PizzaSizeDto> allPizzaSizes = pizzaSizeService.listAllByPizzaId(pizzaId);
		PizzaDto pizza = pizzaService.getPizzaById(pizzaId);
		map.put("allPizzaSizes", allPizzaSizes);
		map.put("pizza", pizza);
		map.put("recordsPerPage", 5);
		return "fragments/pizzaSizeListByPizza :: ajaxLoadedContent";
	}

	@GetMapping(value = "/createPizzaSize")
	public String renderPizzaSizeForm(ModelMap map) {
		PizzaSizeDto pizzaSize = new PizzaSizeDto();
		List<PizzaDto> allPizzas = pizzaService.listAll();
		map.put("pizzaSize", pizzaSize);
		map.put("allPizzas", allPizzas);
		return "fragments/pizzaSizeForm :: ajaxLoadedContent";
	}

	@PostMapping(value = "/createPizzaSize")
	public String storePizzaSize(@ModelAttribute("pizzaSize") PizzaSizeDto pizzaSize) {
		pizzaSizeService.addPizzaSize(pizzaSize);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/updatePizzaSize/{pizzaSizeId}")
	public String renderUpdatePizzaSizeForm(@PathVariable("pizzaSizeId") Integer pizzaSizeId, ModelMap map) {
		PizzaSizeDto pizzaSize = new PizzaSizeDto();
		PizzaSizeDto currentPizzaSize = pizzaSizeService.getPizzaSizeById(pizzaSizeId);
		List<PizzaDto> allPizzas = pizzaService.listAll();
		map.put("pizzaSize", pizzaSize);
		map.put("allPizzas", allPizzas);
		map.put("currentPizzaSize", currentPizzaSize);
		return "fragments/updatePizzaSizeForm :: ajaxLoadedContent";

	}

	@GetMapping(value = "/deletePizzaSize/{pizzaSizeId}")
	public String deletePizzaSize(@PathVariable("pizzaSizeId") Integer pizzaSizeId) {

		pizzaSizeService.deletePizzaSize(pizzaSizeId);

		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/pizzaSizeDetails/{pizzaSizeId}")
	public String getPizzaSizeDetails(@PathVariable("pizzaSizeId") Integer pizzaSizeId, ModelMap map) {
		PizzaSizeDto pizzaSize = pizzaSizeService.getPizzaSizeById(pizzaSizeId);
		PizzaDto pizza = pizzaService.getPizzaById(pizzaSize.getPizzaId());
		map.put("pizzaSize", pizzaSize);
		map.put("pizza", pizza);
		return "fragments/pizzaSizeDetails :: ajaxLoadedContent";
	}

	@GetMapping(value = "/allCustomers")
	public String getAllCustomers(ModelMap map) {
		List<CustomerDto> allCustomers = customerService.getAllCustomers();
		List<UserDto> allUsers = userService.listAllUsers();
		map.put("allCustomers", allCustomers);
		map.put("allUsers", allUsers);
		map.put("recordsPerPage", 8);
		return "fragments/customerList :: ajaxLoadedContent";
	}

	@GetMapping(value = "/customerDetails/{customerId}")
	public String getCustomerDetails(@PathVariable("customerId") Integer customerId, ModelMap map) {
		CustomerDto customer = customerService.getCustomer(customerId);
		UserDto user = userService.getUserById(customer.getUserId());
		map.put("customer", customer);
		map.put("user", user);
		return "fragments/customerDetails :: ajaxLoadedContent";
	}

	@GetMapping(value = "/suspendUser/{userId}")
	public String suspendUser(@PathVariable("userId") Integer userId) {
		userService.suspendUser(userId);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/clearSuspension/{userId}")
	public String removeSuspension(@PathVariable("userId") Integer userId) {
		userService.clearSuspension(userId);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/allOrders")
	public String listAllOrders(ModelMap map) {
		List<OrderDto> allOrders = orderService.listAll();
		List<CustomerDto> allCustomers = customerService.getAllCustomers();
		List<UserDto> allUsers = userService.listAllUsers();
		map.put("allOrders", allOrders);
		map.put("allCustomers", allCustomers);
		map.put("allUsers", allUsers);
		map.put("recordsPerPage", 8);
		return "fragments/orderList :: ajaxLoadedContent";
	}

	@GetMapping(value = "/deleteOrder/{orderId}")
	public String removeOrder(@PathVariable("orderId") Integer orderId) {
		orderService.deleteOrder(orderId);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/orderDetails/{orderId}")
	public String getOrderDetails(@PathVariable("orderId") Integer orderId, ModelMap map) {
		OrderDto order = orderService.getOrder(orderId);
		OrderAddressDto address = orderAddressService.getAddressById(order.getAddressId());
		List<OrderItemDto> orderedItems = orderItemService.listAllByOrderId(orderId);
		map.put("order", order);
		map.put("address", address);
		map.put("orderedItems", orderedItems);
		return "fragments/orderDetails :: ajaxLoadedContent";
	}

	@GetMapping(value = "/existingSizeError")
	public String sizeError() {
		return "fragments/pizzaSizeError :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/deleteCustomer/{customerId}", method = RequestMethod.GET)
	public String removeCustomer(@PathVariable("customerId") Integer customerId) {
		customerService.deleteCustomer(customerId);
		return "fragments/homePage :: ajaxLoadedContent";
	}

}
