package com.klybik.management.dto.developmentPlan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDevelopmentPlanRequest {
    private String goal;
    private String comments;
    private UUID employeeUserId;
    private UUID surveyId;
    private UUID competencyId;
}
