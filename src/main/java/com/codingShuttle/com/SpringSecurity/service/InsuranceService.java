package com.codingShuttle.com.SpringSecurity.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.codingShuttle.com.SpringSecurity.entity.Insurance;
import com.codingShuttle.com.SpringSecurity.entity.Patient;
import com.codingShuttle.com.SpringSecurity.repository.InsuranceRepository;
import com.codingShuttle.com.SpringSecurity.repository.PatientRepository;

@Service
@RequiredArgsConstructor
public class InsuranceService {

    private final InsuranceRepository insuranceRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public Patient assignInsuranceToPatient(Insurance insurance, Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));

        patient.setInsurance(insurance);
        insurance.setPatient(patient); // bidirectional consistency maintainence

        return patient;
    }

    @Transactional
    public Patient disaccociateInsuranceFromPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));

        patient.setInsurance(null);
        return patient;
    }

    // HW
    //Create three appointment for a patient and then delete Patient


}
