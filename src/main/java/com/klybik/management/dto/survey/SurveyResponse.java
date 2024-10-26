package com.klybik.management.dto.survey;

import com.klybik.management.constant.enums.EvaluationMethodEnum;
import com.klybik.management.constant.enums.SurveyStatusEnum;
import com.klybik.management.dto.question.QuestionResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SurveyResponse {
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private EvaluationMethodEnum evaluationMethod;
    private SurveyStatusEnum status;
    private List<QuestionResponse> questions;
}
