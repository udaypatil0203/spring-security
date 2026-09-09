package com.codingShuttle.com.SpringSecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codingShuttle.com.SpringSecurity.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
