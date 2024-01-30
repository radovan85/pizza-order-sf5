package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.OrderDto;

public interface OrderService {

	OrderDto addCustomerOrder();

	List<OrderDto> listAll();

	OrderDto getOrder(Integer orderId);

	void deleteOrder(Integer orderId);

	List<OrderDto> listAllByCartId(Integer cartId);

}
