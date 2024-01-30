package com.radovan.spring.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.exceptions.InvalidUserException;
import com.radovan.spring.repository.UserRepository;
import com.radovan.spring.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TempConverter tempConverter;

	@Override
	public UserDto getUserById(Integer id) {
		// TODO Auto-generated method stub
		Optional<UserEntity> userOpt = userRepository.findById(id);
		UserDto returnValue = null;

		if (userOpt.isPresent()) {
			returnValue = tempConverter.userEntityToDto(userOpt.get());
		} else {
			Error error = new Error("Invalid User");
			throw new InvalidUserException(error);
		}

		return returnValue;
	}

	@Override
	public List<UserDto> listAllUsers() {
		// TODO Auto-generated method stub
		Optional<List<UserEntity>> allUsersOpt = Optional.ofNullable(userRepository.findAll());
		List<UserDto> returnValue = new ArrayList<UserDto>();

		if (!allUsersOpt.isEmpty()) {
			allUsersOpt.get().forEach((userEntity) -> {
				UserDto userDto = tempConverter.userEntityToDto(userEntity);
				returnValue.add(userDto);
			});
		}
		return returnValue;
	}

	@Override
	public UserEntity getUserByEmail(String email) {
		// TODO Auto-generated method stub

		Optional<UserEntity> userOpt = Optional.ofNullable(userRepository.findByEmail(email));
		UserEntity returnValue = null;

		if (userOpt.isPresent()) {
			returnValue = userOpt.get();
		} else {
			Error error = new Error("Invalid User");
			throw new InvalidUserException(error);
		}

		return returnValue;
	}

	@Override
	public UserDto getCurrentUser() {
		// TODO Auto-generated method stub
		UserEntity authUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserEntity userEntity = userRepository.findById(authUser.getId()).get();
		UserDto returnValue = tempConverter.userEntityToDto(userEntity);
		return returnValue;
	}

	@Override
	public void suspendUser(Integer userId) {
		// TODO Auto-generated method stub
		UserEntity userEntity = userRepository.findById(userId).get();
		userEntity.setEnabled((byte) 0);
		userRepository.saveAndFlush(userEntity);
	}

	@Override
	public void clearSuspension(Integer userId) {
		// TODO Auto-generated method stub
		UserEntity userEntity = userRepository.findById(userId).get();
		userEntity.setEnabled((byte) 1);
		userRepository.saveAndFlush(userEntity);
	}

}