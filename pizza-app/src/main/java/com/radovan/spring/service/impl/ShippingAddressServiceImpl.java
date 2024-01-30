package com.radovan.spring.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.ShippingAddressDto;
import com.radovan.spring.entity.ShippingAddressEntity;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.ShippingAddressRepository;
import com.radovan.spring.service.ShippingAddressService;

@Service
@Transactional
public class ShippingAddressServiceImpl implements ShippingAddressService {

	@Autowired
	private ShippingAddressRepository addressRepository;

	@Autowired
	private TempConverter tempConverter;

	@Override
	public ShippingAddressDto addShippingAddress(ShippingAddressDto shippingAddress) {
		// TODO Auto-generated method stub
		ShippingAddressEntity addressEntity = tempConverter.shippingAddressDtoToEntity(shippingAddress);
		ShippingAddressEntity storedAddress = addressRepository.save(addressEntity);
		ShippingAddressDto returnValue = tempConverter.shippingAddressEntityToDto(storedAddress);
		return returnValue;
	}

	@Override
	public ShippingAddressDto getShippingAddress(Integer addressId) {
		// TODO Auto-generated method stub
		Optional<ShippingAddressEntity> addressOpt = addressRepository.findById(addressId);
		ShippingAddressDto returnValue = null;

		if (addressOpt.isPresent()) {
			returnValue = tempConverter.shippingAddressEntityToDto(addressOpt.get());
		} else {
			Error error = new Error("The shipping address has not been found!");
			throw new InstanceUndefinedException(error);
		}
		return returnValue;
	}

}