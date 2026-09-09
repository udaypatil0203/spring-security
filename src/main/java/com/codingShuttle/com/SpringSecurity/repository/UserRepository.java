package com.codingShuttle.com.SpringSecurity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codingShuttle.com.SpringSecurity.entity.User;
import com.codingShuttle.com.SpringSecurity.entity.type.AuthProviderType;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	Optional<User> findByProviderIdAndProviderType(String providerId, AuthProviderType providerType);

}
