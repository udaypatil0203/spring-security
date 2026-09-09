package com.codingShuttle.com.SpringSecurity.entity;

import java.security.AuthProvider;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.codingShuttle.com.SpringSecurity.entity.type.AuthProviderType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long  id;
	
	@JoinColumn(unique = true)
	private String username;
	private String password;
	
	private String providerId;
	
	@Enumerated(EnumType.STRING)
	private AuthProviderType providerType;
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    return List.of();
	}

}
