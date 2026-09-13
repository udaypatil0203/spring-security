package com.codingShuttle.com.SpringSecurity.Controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingShuttle.com.SpringSecurity.dto.AppointmentResponseDto;
import com.codingShuttle.com.SpringSecurity.entity.User;
import com.codingShuttle.com.SpringSecurity.service.AppointmentService;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final AppointmentService appointmentService;

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointmentsOfDoctor() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();//u can acess the user info from SecurityContextHolder
        																				// u will get the info of currently login user	
        return ResponseEntity.ok(appointmentService.getAllAppointmentsOfDoctor(user.getId()));
    }

}
