package com.radovan.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

	UserEntity findByEmail(String email);

	@Modifying
	@Query(value = "delete from users_roles where user_id = :userId", nativeQuery = true)
	void clearUserRoles(@Param("userId") Integer userId);
}
