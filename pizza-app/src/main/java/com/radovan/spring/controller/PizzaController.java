package com.radovan.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.dto.PizzaDto;
import com.radovan.spring.dto.PizzaSizeDto;
import com.radovan.spring.service.PizzaService;
import com.radovan.spring.service.PizzaSizeService;

@Controller
@RequestMapping(value = "/pizzas")
public class PizzaController {

	@Autowired
	private PizzaService pizzaService;

	@Autowired
	private PizzaSizeService pizzaSizeService;

	@GetMapping(value = "/allPizzas")
	public String listAllPizzas(ModelMap map) {
		List<PizzaDto> allPizzas = pizzaService.listAll();
		map.put("allPizzas", allPizzas);
		map.put("recordsPerPage", 5);
		return "fragments/pizzaListUser :: ajaxLoadedContent";
	}

	@GetMapping(value = "/pizzaDetails/{pizzaId}")
	public String getPizzaDetails(@PathVariable("pizzaId") Integer pizzaId, ModelMap map) {
		PizzaDto pizza = pizzaService.getPizzaById(pizzaId);
		List<PizzaSizeDto> pizzaSizes = pizzaSizeService.listAllByPizzaId(pizzaId);
		map.put("pizza", pizza);
		map.put("pizzaSizes", pizzaSizes);
		return "fragments/pizzaDetailsUser :: ajaxLoadedContent";
	}

	@GetMapping(value = "/orderPizza/{pizzaId}")
	public String orderPizza(@PathVariable("pizzaId") Integer pizzaId, ModelMap map) {
		CartItemDto cartItem = new CartItemDto();
		List<PizzaSizeDto> allPizzaSizes = pizzaSizeService.listAllByPizzaId(pizzaId);
		PizzaDto pizza = pizzaService.getPizzaById(pizzaId);
		map.put("allPizzaSizes", allPizzaSizes);
		map.put("pizza", pizza);
		map.put("cartItem", cartItem);
		return "fragments/orderPizza :: ajaxLoadedContent";
	}

	@GetMapping(value = "/searchPizza")
	public String searchPizza(@RequestParam("keyword") String keyword, ModelMap map) {
		List<PizzaDto> allPizzas = pizzaService.listAllByKeyword(keyword);
		map.put("allPizzas", allPizzas);
		map.put("recordsPerPage", 5);
		return "fragments/searchResult :: ajaxLoadedContent";
	}
}
