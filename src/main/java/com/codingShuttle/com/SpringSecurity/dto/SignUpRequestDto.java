package com.codingShuttle.com.SpringSecurity.dto;

import java.util.HashSet;
import java.util.Set;

import com.codingShuttle.com.SpringSecurity.entity.type.RoleType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {
	private String username;
	private String Password;
	private String name;
	
	private Set<RoleType> roles = new HashSet<>();

}
