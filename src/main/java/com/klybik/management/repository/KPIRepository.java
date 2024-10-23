package com.klybik.management.repository;

import com.klybik.management.entity.KPI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface KPIRepository extends JpaRepository<KPI, UUID> {
    List<KPI> findByEmployeeId(UUID employeeId);
}