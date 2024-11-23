package com.klybik.management.repository;

import com.klybik.management.entity.DevelopmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DevelopmentPlanRepository extends JpaRepository<DevelopmentPlan, UUID> {
    List<DevelopmentPlan> findAllByEmployeeUserIdAndSurveyId(UUID employeeUserId, UUID surveyId);
}
