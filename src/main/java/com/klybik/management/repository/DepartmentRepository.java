package com.klybik.management.repository;

import com.klybik.management.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    boolean existsByNameIgnoreCase(String name);
}
