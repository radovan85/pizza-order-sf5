package com.radovan.spring.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.dto.PizzaSizeDto;
import com.radovan.spring.entity.PizzaSizeEntity;
import com.radovan.spring.exceptions.ExistingSizeException;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.PizzaSizeRepository;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.PizzaSizeService;

@Service
@Transactional
public class PizzaSizeServiceImpl implements PizzaSizeService {

	@Autowired
	private PizzaSizeRepository pizzaSizeRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private CartService cartService;

	@Override
	public PizzaSizeDto addPizzaSize(PizzaSizeDto pizzaSize) {
		// TODO Auto-generated method stub
		Optional<Integer> sizeIdOptional = Optional.ofNullable(pizzaSize.getPizzaSizeId());

		Optional<PizzaSizeEntity> sizeOpt = Optional
				.ofNullable(pizzaSizeRepository.findByNameAndPizzaId(pizzaSize.getName(), pizzaSize.getPizzaId()));
		if (sizeOpt.isPresent() && !sizeIdOptional.isPresent()) {
			Error error = new Error("Size already exists");
			throw new ExistingSizeException(error);
		}
		PizzaSizeEntity sizeEntity = tempConverter.pizzaSizeDtoToEntity(pizzaSize);
		PizzaSizeEntity storedSize = pizzaSizeRepository.save(sizeEntity);
		PizzaSizeDto returnValue = tempConverter.pizzaSizeEntityToDto(storedSize);

		if (sizeIdOptional.isPresent()) {
			Integer sizeId = sizeIdOptional.get();
			List<CartItemDto> allItems = cartItemService.listAllByPizzaSizeId(sizeId);
			allItems.forEach((item) -> {
				item.setPrice(pizzaSize.getPrice() * item.getQuantity());
				cartItemService.addCartItem(item);
			});

			cartService.refreshAllCartStates();
		}
		return returnValue;
	}

	@Override
	public PizzaSizeDto getPizzaSizeById(Integer pizzaSizeId) {
		// TODO Auto-generated method stub
		PizzaSizeDto returnValue = null;
		Optional<PizzaSizeEntity> sizeOpt = pizzaSizeRepository.findById(pizzaSizeId);
		if (sizeOpt.isPresent()) {
			returnValue = tempConverter.pizzaSizeEntityToDto(sizeOpt.get());
		} else {
			Error error = new Error("The pizza size has not been found");
			throw new InstanceUndefinedException(error);
		}
		return returnValue;
	}

	@Override
	public void deletePizzaSize(Integer pizzaSizeId) {
		// TODO Auto-generated method stub
		cartItemService.eraseAllByPizzaSizeId(pizzaSizeId);
		pizzaSizeRepository.deleteById(pizzaSizeId);
		pizzaSizeRepository.flush();
		cartService.refreshAllCartStates();
	}

	@Override
	public List<PizzaSizeDto> listAll() {
		// TODO Auto-generated method stub
		List<PizzaSizeDto> returnValue = new ArrayList<PizzaSizeDto>();
		Optional<List<PizzaSizeEntity>> allSizesOpt = Optional.ofNullable(pizzaSizeRepository.findAll());
		if (!allSizesOpt.isEmpty()) {
			allSizesOpt.get().forEach((size) -> {
				PizzaSizeDto sizeDto = tempConverter.pizzaSizeEntityToDto(size);
				returnValue.add(sizeDto);
			});
		}
		return returnValue;
	}

	@Override
	public List<PizzaSizeDto> listAllByPizzaId(Integer pizzaId) {
		// TODO Auto-generated method stub
		List<PizzaSizeDto> returnValue = new ArrayList<PizzaSizeDto>();
		Optional<List<PizzaSizeEntity>> allSizesOpt = Optional
				.ofNullable(pizzaSizeRepository.findAllByPizzaId(pizzaId));
		if (!allSizesOpt.isEmpty()) {
			allSizesOpt.get().forEach((size) -> {
				PizzaSizeDto sizeDto = tempConverter.pizzaSizeEntityToDto(size);
				returnValue.add(sizeDto);
			});
		}
		return returnValue;
	}

	@Override
	public void deleteAllByPizzaId(Integer pizzaId) {
		// TODO Auto-generated method stub
		List<PizzaSizeDto> allSizes = listAllByPizzaId(pizzaId);
		allSizes.forEach((size) -> {
			deletePizzaSize(size.getPizzaSizeId());
		});

		cartService.refreshAllCartStates();
	}

}
