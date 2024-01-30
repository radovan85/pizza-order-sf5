package com.radovan.spring.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.CartDto;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.OrderAddressDto;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.dto.OrderItemDto;
import com.radovan.spring.dto.ShippingAddressDto;

import com.radovan.spring.entity.OrderAddressEntity;
import com.radovan.spring.entity.OrderEntity;
import com.radovan.spring.entity.OrderItemEntity;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.OrderAddressRepository;
import com.radovan.spring.repository.OrderItemRepository;
import com.radovan.spring.repository.OrderRepository;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.OrderService;
import com.radovan.spring.service.ShippingAddressService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderAddressRepository orderAddressRepository;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ShippingAddressService shippingAddressService;

	@Override
	public OrderDto addCustomerOrder() {
		// TODO Auto-generated method stub
		CustomerDto customer = customerService.getCurrentCustomer();
		CartDto cart = cartService.getCartByCartId(customer.getCartId());
		OrderDto order = new OrderDto();
		List<OrderItemEntity> orderedItems = new ArrayList<OrderItemEntity>();

		List<CartItemDto> cartItems = cartItemService.listAllByCartId(cart.getCartId());

		if (!cartItems.isEmpty()) {
			for (CartItemDto item : cartItems) {
				OrderItemDto orderedItem = tempConverter.cartItemToOrderItemDto(item);
				OrderItemEntity orderedItemEntity = tempConverter.orderItemDtoToEntity(orderedItem);
				OrderItemEntity storedOrderedItem = orderItemRepository.save(orderedItemEntity);
				orderedItems.add(storedOrderedItem);
			}
		}

		ShippingAddressDto shippingAddress = shippingAddressService.getShippingAddress(customer.getShippingAddressId());
		OrderAddressDto orderAddress = tempConverter.shippingAddressToOrderAddress(shippingAddress);
		OrderAddressEntity orderAddressEntity = tempConverter.orderAddressDtoToEntity(orderAddress);
		OrderAddressEntity storedOrderAddress = orderAddressRepository.save(orderAddressEntity);

		order.setCartId(cart.getCartId());
		order.setOrderPrice(cart.getCartPrice());
		OrderEntity orderEntity = tempConverter.orderDtoToEntity(order);
		orderEntity.setOrderedItems(orderedItems);
		orderEntity.setAddress(storedOrderAddress);

		OrderEntity storedOrder = orderRepository.save(orderEntity);

		storedOrderAddress.setOrder(storedOrder);
		orderAddressRepository.saveAndFlush(storedOrderAddress);

		OrderDto returnValue = tempConverter.orderEntityToDto(storedOrder);

		storedOrder.getOrderedItems().forEach((item) -> {
			item.setOrder(storedOrder);
			orderItemRepository.saveAndFlush(item);
		});

		cartItems.forEach((item) -> {
			cartItemService.removeCartItem(item.getCartItemId());
		});

		cartService.refreshCartState(cart.getCartId());
		return returnValue;
	}

	@Override
	public List<OrderDto> listAll() {
		// TODO Auto-generated method stub
		List<OrderEntity> allOrders = orderRepository.findAll();
		List<OrderDto> returnValue = new ArrayList<>();

		if (!allOrders.isEmpty()) {
			allOrders.forEach((order) -> {
				OrderDto orderDto = tempConverter.orderEntityToDto(order);
				returnValue.add(orderDto);
			});
		}

		return returnValue;
	}

	@Override
	public OrderDto getOrder(Integer orderId) {
		// TODO Auto-generated method stub
		OrderDto returnValue = null;
		Optional<OrderEntity> orderOptional = orderRepository.findById(orderId);
		if (orderOptional.isPresent()) {
			returnValue = tempConverter.orderEntityToDto(orderOptional.get());
		} else {
			Error error = new Error("The order has not been found!");
			throw new InstanceUndefinedException(error);
		}

		return returnValue;
	}

	@Override
	public void deleteOrder(Integer orderId) {
		// TODO Auto-generated method stub
		orderRepository.deleteById(orderId);
		orderRepository.flush();

	}

	@Override
	public List<OrderDto> listAllByCartId(Integer cartId) {
		// TODO Auto-generated method stub
		List<OrderEntity> allOrders = orderRepository.findAllByCartId(cartId);
		List<OrderDto> returnValue = new ArrayList<>();

		if (!allOrders.isEmpty()) {
			allOrders.forEach((order) -> {
				OrderDto orderDto = tempConverter.orderEntityToDto(order);
				returnValue.add(orderDto);
			});
		}

		return returnValue;
	}

}
