package com.codingShuttle.com.SpringSecurity.dto;

import lombok.Data;

import java.time.LocalDate;

import com.codingShuttle.com.SpringSecurity.entity.type.BloodGroupType;

@Data
public class PatientResponseDto {
    private Long id;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private BloodGroupType bloodGroup;
}
