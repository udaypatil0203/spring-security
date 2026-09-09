package com.codingShuttle.com.SpringSecurity.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codingShuttle.com.SpringSecurity.dto.AppointmentResponseDto;
import com.codingShuttle.com.SpringSecurity.dto.CreateAppointmentRequestDto;
import com.codingShuttle.com.SpringSecurity.dto.PatientResponseDto;
import com.codingShuttle.com.SpringSecurity.service.AppointmentService;
import com.codingShuttle.com.SpringSecurity.service.PatientService;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final AppointmentService appointmentService;

    @PostMapping("/appointments")
    public ResponseEntity<AppointmentResponseDto> createNewAppointment(@RequestBody CreateAppointmentRequestDto createAppointmentRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.createNewAppointment(createAppointmentRequestDto));
    }

    @GetMapping("/profile")
    private ResponseEntity<PatientResponseDto> getPatientProfile() {
        Long patientId = 4L;
        return ResponseEntity.ok(patientService.getPatientById(patientId));
    }

}