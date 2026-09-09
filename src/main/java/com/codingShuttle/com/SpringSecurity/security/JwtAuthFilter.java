package com.codingShuttle.com.SpringSecurity.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.codingShuttle.com.SpringSecurity.entity.User;
import com.codingShuttle.com.SpringSecurity.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j//simple login framework for java
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{  //This class is a JWT authentication filter in Spring Security. Its main job is:
						//For every incoming HTTP request, check whether the request contains a JWT token in the Authorization header. 
						//If it does, extract the token and find out which username is associated with that token.

	private final UserRepository userRepository;
	private final AuthUtil authUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("incoming request: {}", request.getRequestURI());
		
		final String requestTokenHeader = request.getHeader("Authorization");
		if(requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String token = requestTokenHeader.substring(7).trim();
		String username = authUtil.getUsernameFromToken(token);
		
		if(username != null && SecurityContextHolder.getContext().getAuthentication()==null ) {
			User user = userRepository.findByUsername(username).orElseThrow();
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken 
					= new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			
		
		}
		
		filterChain.doFilter(request, response);
		
	}
	
	
}


