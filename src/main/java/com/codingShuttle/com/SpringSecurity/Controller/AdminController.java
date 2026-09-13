package com.codingShuttle.com.SpringSecurity.Controller;




import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.codingShuttle.com.SpringSecurity.dto.DoctorResponseDto;
import com.codingShuttle.com.SpringSecurity.dto.OnboardDoctorRequestDto;
import com.codingShuttle.com.SpringSecurity.dto.PatientResponseDto;
import com.codingShuttle.com.SpringSecurity.entity.User;
import com.codingShuttle.com.SpringSecurity.service.DoctorService;
import com.codingShuttle.com.SpringSecurity.service.PatientService;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PatientService patientService;
    private final DoctorService doctorService;

    @GetMapping("/patients")
    public ResponseEntity<List<PatientResponseDto>> getAllPatients(
            @RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "size", defaultValue = "10") Integer pageSize
    ) {
    	//after going through all the security layer finally the request comes to the controller layer 
    	//and once ur request reaches to the the controller u can access SecurityContextHolder
    	//and can fetch the data
    	
//    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();        
    	
    	return ResponseEntity.ok(patientService.getAllPatients(pageNumber, pageSize));
    }

    @PostMapping("/onBoardNewDoctor")
    public ResponseEntity<DoctorResponseDto> onBoardNewDoctor(@RequestBody OnboardDoctorRequestDto onboardDoctorRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.onBoardNewDoctor(onboardDoctorRequestDto));
    }
}
