package com.codingShuttle.com.SpringSecurity.security;

import java.security.Permissions;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.codingShuttle.com.SpringSecurity.entity.type.PermissionType;
import com.codingShuttle.com.SpringSecurity.entity.type.RoleType;

import static com.codingShuttle.com.SpringSecurity.entity.type.PermissionType.*;
import static com.codingShuttle.com.SpringSecurity.entity.type.RoleType.*;

public class RolePermissionMapping {

    // Maps each role to the set of permissions assigned to that role.
    private static final Map<RoleType, Set<PermissionType>> map = Map.of(

        // PATIENT can read patient information and read/write appointments.
        PATIENT, Set.of(
            PATIENT_READ,
            APPOINTMENT_READ,
            APPOINTMENT_WRITE
        ),

        // DOCTOR can read patients, read/write appointments,
        // and delete appointments.
        DOCTOR, Set.of(
            APPOINTMENT_DELETE,
            APPOINTMENT_WRITE,
            APPOINTMENT_READ,
            PATIENT_READ
        ),

        // ADMIN has all available permissions.
        ADMIN, Set.of(
            PATIENT_READ,
            APPOINTMENT_READ,
            APPOINTMENT_DELETE,
            APPOINTMENT_WRITE,
            USER_MANAGE,
            REPORT_VIEW
        )
    );

	public static Set<SimpleGrantedAuthority> getAuthoritiesForRole(RoleType role) {
		return map.get(role).stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
	}

    
}