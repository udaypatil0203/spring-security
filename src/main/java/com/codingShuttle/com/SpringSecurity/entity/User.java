package com.codingShuttle.com.SpringSecurity.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.codingShuttle.com.SpringSecurity.entity.type.AuthProviderType;
import com.codingShuttle.com.SpringSecurity.entity.type.PermissionType;
import com.codingShuttle.com.SpringSecurity.entity.type.RoleType;
import com.codingShuttle.com.SpringSecurity.security.RolePermissionMapping;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Username is unique, so two users cannot have the same username.
    @JoinColumn(unique = true)
    private String username;

    private String password;

    // Stores the provider-specific ID when OAuth2 login is used.
    private String providerId;

    // Stores which authentication provider was used,
    // such as GOOGLE, GITHUB, etc.
    @Enumerated(EnumType.STRING)
    private AuthProviderType providerType;

    /*
     * Stores the roles of the user in a separate collection table.
     *
     * EAGER means roles are fetched whenever the User is fetched.
     *
     * Example:
     * User → Roles
     *        ADMIN
     *        DOCTOR
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    Set<RoleType> roles = new HashSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

    	//return roles.stream()
    	//.map(roles -> new SimpleGrantedAuthority("ROLE_"+roles.name()))
    	//.collect(Collectors.toSet());
    	
        /*
         * This Set will contain both:
         *
         * 1. Roles
         * 2. Permissions
         *
         * Example:
         *
         * ROLE_DOCTOR
         * PATIENT_READ
         * APPOINTMENT_READ
         * APPOINTMENT_WRITE
         * APPOINTMENT_DELETE
         */
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();


        /*
         * Loop through every role assigned to the user.
         */
        roles.forEach(role -> {

            /*
             * Get all permissions associated with this role
             * from RolePermissionMapping.
             */
            Set<SimpleGrantedAuthority> permissions =
                    RolePermissionMapping.getAuthoritiesForRole(role);
                            

            /*
             * Add all permissions to the user's authorities.
             */
            authorities.addAll(permissions);


            /*
             * Add the role itself as an authority.
             *
             * "ROLE_" prefix is required because Spring Security's
             * hasRole() internally looks for ROLE_<role>.
             *
             * Example:
             *
             * RoleType.DOCTOR
             *        ↓
             * ROLE_DOCTOR
             */
            authorities.add(
                new SimpleGrantedAuthority(
                    "ROLE_" + role.name()
                )
            );
        });


        /*
         * Return all roles and permissions of the user.
         */
        return authorities;
    }
}


