package com.radovan.spring.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.PizzaDto;
import com.radovan.spring.entity.PizzaEntity;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.PizzaRepository;
import com.radovan.spring.service.PizzaService;
import com.radovan.spring.service.PizzaSizeService;

@Service
@Transactional
public class PizzaServiceImpl implements PizzaService {

	@Autowired
	private PizzaRepository pizzaRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private PizzaSizeService pizzaSizeService;

	@Override
	public List<PizzaDto> listAll() {
		// TODO Auto-generated method stub
		Optional<List<PizzaEntity>> allPizzasOpt = Optional.ofNullable(pizzaRepository.findAll());
		List<PizzaDto> returnValue = new ArrayList<PizzaDto>();

		if (!allPizzasOpt.isEmpty()) {
			allPizzasOpt.get().forEach((pizza) -> {
				PizzaDto pizzaDto = tempConverter.pizzaEntityToDto(pizza);
				returnValue.add(pizzaDto);
			});
		}
		return returnValue;
	}

	@Override
	public PizzaDto getPizzaById(Integer pizzaId) {
		// TODO Auto-generated method stub
		PizzaDto returnValue = null;
		Optional<PizzaEntity> pizzaOpt = pizzaRepository.findById(pizzaId);
		if (pizzaOpt.isPresent()) {
			returnValue = tempConverter.pizzaEntityToDto(pizzaOpt.get());
		} else {
			Error error = new Error("The pizza has not been found!");
			throw new InstanceUndefinedException(error);
		}
		return returnValue;
	}

	@Override
	public void deletePizza(Integer pizzaId) {
		// TODO Auto-generated method stub

		pizzaSizeService.deleteAllByPizzaId(pizzaId);
		pizzaRepository.deleteById(pizzaId);
		pizzaRepository.flush();
	}

	@Override
	public PizzaDto addPizza(PizzaDto pizza) {
		// TODO Auto-generated method stub
		PizzaEntity pizzaEntity = tempConverter.pizzaDtoToEntity(pizza);
		PizzaEntity storedPizza = pizzaRepository.save(pizzaEntity);
		PizzaDto returnValue = tempConverter.pizzaEntityToDto(storedPizza);
		return returnValue;
	}

	@Override
	public List<PizzaDto> listAllByKeyword(String keyword) {
		// TODO Auto-generated method stub
		Optional<List<PizzaEntity>> allPizzasOpt = Optional.ofNullable(pizzaRepository.findAllByKeyword(keyword));
		List<PizzaDto> returnValue = new ArrayList<PizzaDto>();

		if (!allPizzasOpt.isEmpty()) {
			allPizzasOpt.get().forEach((pizza) -> {
				PizzaDto pizzaDto = tempConverter.pizzaEntityToDto(pizza);
				returnValue.add(pizzaDto);
			});
		}
		return returnValue;
	}

}
