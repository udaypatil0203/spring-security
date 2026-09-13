package com.codingShuttle.com.SpringSecurity.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingShuttle.com.SpringSecurity.dto.LoginRequestDto;
import com.codingShuttle.com.SpringSecurity.dto.LoginResponseDto;
import com.codingShuttle.com.SpringSecurity.dto.SignUpRequestDto;
import com.codingShuttle.com.SpringSecurity.dto.SignupResponseDto;
import com.codingShuttle.com.SpringSecurity.security.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
		
		  System.out.println("🔥 SIGNUP CONTROLLER REACHED");
		return ResponseEntity.ok(authService.login(loginRequestDto));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<SignupResponseDto> signup(@RequestBody SignUpRequestDto signupRequestDto){
		return ResponseEntity.ok(authService.signup(signupRequestDto));
	}
}
