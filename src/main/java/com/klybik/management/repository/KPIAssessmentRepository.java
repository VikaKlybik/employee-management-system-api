package com.klybik.management.repository;

import com.klybik.management.entity.KPIAssessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface KPIAssessmentRepository extends JpaRepository<KPIAssessment, UUID> {
}
