package com.codingShuttle.com.SpringSecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codingShuttle.com.SpringSecurity.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
