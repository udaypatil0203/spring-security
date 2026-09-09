package com.codingShuttle.com.SpringSecurity.security;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingShuttle.com.SpringSecurity.dto.LoginRequestDto;
import com.codingShuttle.com.SpringSecurity.dto.LoginResponseDto;
import com.codingShuttle.com.SpringSecurity.dto.SignupResponseDto;
import com.codingShuttle.com.SpringSecurity.entity.User;
import com.codingShuttle.com.SpringSecurity.entity.type.AuthProviderType;
import com.codingShuttle.com.SpringSecurity.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {  //Take username + password → authenticate the user → generate JWT → return JWT to the client.
	
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;
	private final AuthUtil authUtil;
//	AuthenticationManager
//    		↓
//	AuthenticationProvider
//    	↓
//	UserDetailsService
//    	↓
//	Database
//    	↓
//	Find user
//    	↓
//	PasswordEncoder
//    	↓
//	Check password
	
	public  LoginResponseDto login(LoginRequestDto loginRequestDto) {
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())		
		);
		
		User user = (User) authentication.getPrincipal();//gives you the identity of the authenticated user
		
		String token = authUtil.generateAccessToken(user);
		
		return new LoginResponseDto(token, user.getId());
				
	}
	
	public User signUpInternal(LoginRequestDto signupRequestDto, AuthProviderType authProviderType, String providerId) {
		
		User user = userRepository.findByUsername(signupRequestDto.getUsername()).orElse(null);

	    if (user != null) {
	        throw new IllegalArgumentException("User already exists");
	    }

	    user = User.builder()
                .username(signupRequestDto.getUsername())
                .providerId(providerId)
                .providerType(authProviderType)
                .build();
	    
	    if(authProviderType == AuthProviderType.EMAIL) {
	    	user.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
	    }
	    
	    return userRepository.save(user);
	}
	
	//login Controller
	public SignupResponseDto signup(LoginRequestDto signupRequestDto) {
		User user = signUpInternal(signupRequestDto, AuthProviderType.EMAIL, null); 
	    

	    return new SignupResponseDto(user.getId(),user.getUsername());
	}

	
	// for OAuth2:
	@Transactional
	public ResponseEntity<LoginResponseDto> handleOAuth2LoginRequest(OAuth2User oAuth2User, String registrationId) {
		
		// fetch providerType and providerId
		// save the providerType and providerId info with user
		//if the user has an account : directly login
		
		//otherwise, first signup and then login
		
		AuthProviderType providerType = authUtil.getProviderTypeFromRegistrationId(registrationId);
		String providerId = authUtil.determineProviderIdFromAuth2User(oAuth2User, registrationId);
		
		User user = userRepository.findByProviderIdAndProviderType(providerId, providerType).orElse(null);
		String email = oAuth2User.getAttribute("email");
		
		User emailUser = userRepository.findByUsername(email).orElse(null);
		
		if(user == null && emailUser == null) {
			//signup flow:
			String username = authUtil.determineUsernameFromOAuth2User(oAuth2User, registrationId, providerId);
			user = signUpInternal(new LoginRequestDto(username, null),providerType, providerId);
		}else if(user != null) {
			if(email != null && !email.isBlank() && !email.equals(user.getUsername())) {
				user.setUsername(email);
				userRepository.save(user);
			}
		}else {//to hnadle the case where email = null and emailUser != null
			throw new BadCredentialsException("This email is already registered with provider "+emailUser.getProviderType());
		}
		
		LoginResponseDto loginResponseDto = new LoginResponseDto(authUtil.generateAccessToken(user), user.getId());
		return ResponseEntity.ok(loginResponseDto);
	}

}
