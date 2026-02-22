package com.employee.manage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.employee.manage.dto.AuthProviderType;
import com.employee.manage.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);

	Optional<User> findByProviderIdAndProviderType(String providerId, AuthProviderType providerType);
}
