package com.codingShuttle.com.SpringSecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codingShuttle.com.SpringSecurity.entity.Insurance;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
}
