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
import com.radovan.spring.entity.CartItemEntity;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CartService;

@Service
@Transactional
public class CartItemServiceImpl implements CartItemService {

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private CartService cartService;

	@Override
	public CartItemDto addCartItem(CartItemDto cartItem) {
		// TODO Auto-generated method stub
		CartItemEntity cartItemEntity = tempConverter.cartItemDtoToEntity(cartItem);
		CartItemEntity storedItem = cartItemRepository.save(cartItemEntity);
		CartItemDto returnValue = tempConverter.cartItemEntityToDto(storedItem);
		return returnValue;
	}

	@Override
	public void removeCartItem(Integer itemId) {
		// TODO Auto-generated method stub

		CartDto cart = cartService.getMyCart();
		cartItemRepository.removeCartItem(itemId);
		cartItemRepository.flush();
		cartService.refreshCartState(cart.getCartId());

	}

	@Override
	public void eraseAllCartItems(Integer cartId) {
		// TODO Auto-generated method stub
		cartItemRepository.removeAllByCartId(cartId);
		cartItemRepository.flush();
		cartService.refreshCartState(cartId);
	}

	@Override
	public List<CartItemDto> listAllByCartId(Integer cartId) {
		// TODO Auto-generated method stub
		Optional<List<CartItemEntity>> cartItemsOpt = Optional.ofNullable(cartItemRepository.findAllByCartId(cartId));
		List<CartItemDto> returnValue = new ArrayList<>();

		if (!cartItemsOpt.isEmpty()) {
			cartItemsOpt.get().forEach((item) -> {
				CartItemDto itemDto = tempConverter.cartItemEntityToDto(item);
				returnValue.add(itemDto);
			});
		}

		return returnValue;
	}

	@Override
	public void eraseAllByPizzaSizeId(Integer pizzaSizeId) {
		// TODO Auto-generated method stub
		cartItemRepository.removeAllByPizzaSizeId(pizzaSizeId);
		cartItemRepository.flush();

		List<CartDto> allCarts = cartService.listAll();
		if (!allCarts.isEmpty()) {
			allCarts.forEach((cart) -> {
				cartService.refreshCartState(cart.getCartId());
			});
		}
	}

	@Override
	public List<CartItemDto> listAllByPizzaSizeId(Integer pizzaSizeId) {
		// TODO Auto-generated method stub
		Optional<List<CartItemEntity>> cartItemsOpt = Optional
				.ofNullable(cartItemRepository.findAllByPizzaSizeId(pizzaSizeId));
		List<CartItemDto> returnValue = new ArrayList<>();

		if (!cartItemsOpt.isEmpty()) {
			cartItemsOpt.get().forEach((item) -> {
				CartItemDto itemDto = tempConverter.cartItemEntityToDto(item);
				returnValue.add(itemDto);
			});
		}

		return returnValue;
	}

}
