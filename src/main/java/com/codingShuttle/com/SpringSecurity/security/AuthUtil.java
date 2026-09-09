package com.codingShuttle.com.SpringSecurity.security;

import java.awt.image.Kernel;
import java.lang.System.Logger;

import java.nio.charset.StandardCharsets;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.security.KeyStore.SecretKeyEntry;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.codingShuttle.com.SpringSecurity.entity.User;
import com.codingShuttle.com.SpringSecurity.entity.type.AuthProviderType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
//use to generate JWT token....
public class AuthUtil {

//	private String jwtSecreteKey = "swdjzgsduyfqwhbaksjyte726534761824y3qhwevi67dq3egdb467tgb";
						// here we can assign but it is not good practice 
						//coz if we post this on github, anyone can create token for ur server using this key
						//so do not mention it here instead put it in environment var(application.properties)
	
	@Value("${jwt.secretKey}")//field injection....@Value is a Spring annotation used to inject a value from configuration into a Java field.
	private String jwtSecretKey;
	
	private SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
	}
	//JWT cryptographic operations don't want an ordinary String. They need a cryptographic key.
//	The purpose of this method is:
//        Convert the secret String into a proper HMAC SecretKey object.
	
//	jwtSecretKey;
//	This is a Java String.
//	But cryptographic algorithms operate on bytes.
//	StandardCharsets.UTF_8 specifies exactly how the characters should be converted into bytes.
	
//	application.properties
//    ↓
//jwt.secretKey=your-secret
//    ↓
//@Value("${jwt.secretKey}")
//    ↓
//String jwtSecretKey
//    ↓
//getBytes(UTF_8)
//    ↓
//byte[]
//    ↓
//Keys.hmacShaKeyFor(...)
//    ↓
//SecretKey
	
	public String generateAccessToken(User user) {
		return Jwts.builder()
				.subject(user.getUsername())
				.claim("userId",user.getId().toString())
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 1000*60*10))
				.signWith(getSecretKey())
				.compact();
	}//"Create a JWT token containing information about the logged-in user, and make it valid for 10 minutes."

	public String getUsernameFromToken(String token) {
		
		Claims claims = Jwts.parser()
				.verifyWith(getSecretKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
		
		return claims.getSubject();
	}
	
	//for OAuth2
	public AuthProviderType getProviderTypeFromRegistrationId(String registrationId) {
		return switch(registrationId.toLowerCase()) {
		case "google" -> AuthProviderType.GOOGLE;
		case "github" -> AuthProviderType.GITHUB;
		case "facebook" -> AuthProviderType.FACEBOOK;
		default -> throw new IllegalArgumentException("Unsupported OAuth2 provider: "+registrationId);
		
		};
	}
	
	
	public String determineProviderIdFromAuth2User(OAuth2User oAuth2User, String registrationId) {
		String providerId = switch(registrationId.toLowerCase()) {
			case "google" -> oAuth2User.getAttribute("sub");
			case "github" -> oAuth2User.getAttribute("id").toString();
			default -> {
				log.error("Unsupported OAuth2 provider: {}", registrationId);
				throw new IllegalArgumentException("Unsupported OAuth2 provider: "+ registrationId);
			}		
		};
		
		if(providerId == null || providerId.isBlank()) {
			log.error("Unable to determine providerId for provider: {}", registrationId);
			throw new IllegalArgumentException("Unable to determine providerId for OAuth2 login");
		}
		return providerId;
	}
	
	public String determineUsernameFromOAuth2User(OAuth2User oAuth2User, String registrationId, String providerId ) {
		String email = oAuth2User.getAttribute("email");
		if(email != null && email.isBlank()) {
			return email;
		}
		
		return switch (registrationId.toLowerCase()) {
			case "google" -> oAuth2User.getAttribute("sub");
			case "github" -> oAuth2User.getAttribute("Login");
			default -> providerId;
		};
		
	}
	
}


