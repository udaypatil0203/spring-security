
package com.codingShuttle.com.SpringSecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.codingShuttle.com.SpringSecurity.entity.type.RoleType;

import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig {

    // Custom JWT filter which checks the JWT from every incoming request
    // and sets the authenticated user in the SecurityContext.
    private final JwtAuthFilter jwtAuthFilter;

    // Handles what should happen after successful OAuth2 login.
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

            // Disables CSRF protection.
            // We are using JWT-based authentication, so we don't need
            // the traditional CSRF protection used with session-based authentication.
            .csrf(csrf -> csrf.disable())


            // Makes the application stateless.
            // Spring Security will not store authentication information
            // in an HTTP session.
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))


            // Defines authorization rules for different URLs.
            .authorizeHttpRequests(auth -> auth

                // Anyone can access these endpoints without authentication.
                .requestMatchers("/public/**", "/auth/**")
                .permitAll()


                // Only users having the ADMIN role can access /admin/**.
                .requestMatchers("/admin/**")
                .hasRole(RoleType.ADMIN.name())


                // Users having either DOCTOR or ADMIN role can access /doctors/**.
                .requestMatchers("/doctors/**")
                .hasAnyRole(
                    RoleType.DOCTOR.name(),
                    RoleType.ADMIN.name()
                )


                // Every other request requires the user to be authenticated.
                .anyRequest()
                .authenticated()
            )


            // Adds our custom JwtAuthFilter before
            // UsernamePasswordAuthenticationFilter.
            //
            // This allows the JWT to be processed and the user to be
            // authenticated before Spring Security performs authorization.
            .addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class
            )


            // Enables OAuth2 login such as Google/GitHub login.
            .oauth2Login(oAuth2 -> oAuth2

                // This method is executed when OAuth2 authentication fails.
                .failureHandler(
                    (request, response, exception) -> {

                        log.error( "OAuth2 error: {}", exception.getMessage());
                        
                        handlerExceptionResolver.resolveException(
                                request,
                                response,
                                null,
                                exception
                        );
                    }
                )

                // This method is executed after successful OAuth2 authentication.
                .successHandler(oAuth2SuccessHandler)
            )


            // Handles exceptions related to authorization.
            .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer

                // This handler is called when an authenticated user
                // tries to access a resource for which they don't
                // have sufficient permissions/role.
            .accessDeniedHandler(
                    (request, response, accessDeniedException) -> {

                    	handlerExceptionResolver.resolveException(
                                request,
                                response,
                                null,
                                accessDeniedException
                        );
                    }
                )
            );


        // Builds the SecurityFilterChain and gives it back to Spring Security.
        return http.build();
    }


}

