package com.klybik.management.dto.developmentPlan;

import com.klybik.management.constant.enums.DevelopmentStatus;
import com.klybik.management.dto.survey.SimpleSurveyResponse;
import com.klybik.management.entity.Competency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DevelopmentPlanResponse {
    private UUID id;
    private String goal;
    private String comments;
    private DevelopmentStatus status;
    private SimpleSurveyResponse survey;
    private Competency competency;
}
