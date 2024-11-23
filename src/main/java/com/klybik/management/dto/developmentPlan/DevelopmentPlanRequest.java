package com.klybik.management.dto.developmentPlan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DevelopmentPlanRequest {
    private UUID userId;
    private UUID surveyId;
}
