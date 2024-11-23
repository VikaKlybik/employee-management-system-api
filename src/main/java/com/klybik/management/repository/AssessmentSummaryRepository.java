package com.klybik.management.repository;

import com.klybik.management.entity.AssessmentSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AssessmentSummaryRepository extends JpaRepository<AssessmentSummary, UUID> {
    List<AssessmentSummary> findByEmployeeUserIdAndSurveyId(UUID userId, UUID surveyId);
}
