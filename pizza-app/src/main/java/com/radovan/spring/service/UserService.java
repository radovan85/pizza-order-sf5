package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.UserDto;
import com.radovan.spring.entity.UserEntity;

public interface UserService {

	UserDto getUserById(Integer id);

	List<UserDto> listAllUsers();

	UserEntity getUserByEmail(String email);

	UserDto getCurrentUser();

	void suspendUser(Integer userId);

	void clearSuspension(Integer userId);
}
