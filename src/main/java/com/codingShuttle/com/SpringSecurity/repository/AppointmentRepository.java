package com.codingShuttle.com.SpringSecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codingShuttle.com.SpringSecurity.entity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
