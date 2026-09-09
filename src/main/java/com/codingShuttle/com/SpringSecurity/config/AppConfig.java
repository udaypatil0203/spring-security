package com.codingShuttle.com.SpringSecurity.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password4j.BcryptPassword4jPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() { // use to encode the passsword as direct password cannot store in memory
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
    	return configuration.getAuthenticationManager();
    }
    
//    @Bean
//	UserDetailsService userDetailsService() {
//		UserDetails user1 = User.withUsername("admin")
//				.password(passwordEncoder().encode("pass"))
//				.roles("ADMIN")
//				.build();
//		
//		UserDetails user2 = User.withUsername("patient")
//				.password(passwordEncoder().encode("pass"))
//				.roles("PATIENT") 
//				.build();
//		
//		return new InMemoryUserDetailsManager(user1, user2);
//		
//	}
//	Is it compulsory?
//			Case 1: In-memory authentication (your example)
//
//			Yes, you need to provide a UserDetailsService implementation (like InMemoryUserDetailsManager) because that's how Spring knows where the users are.
}
