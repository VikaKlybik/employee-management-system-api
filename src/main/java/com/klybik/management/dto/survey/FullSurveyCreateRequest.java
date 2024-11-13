package com.klybik.management.dto.survey;

import com.klybik.management.constant.enums.EvaluationMethodEnum;
import com.klybik.management.dto.question.CompositeCreateQuestionRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FullSurveyCreateRequest {
    private String name;
    private String description;
    private EvaluationMethodEnum evaluationMethod;
    private List<CompositeCreateQuestionRequest> questions;
}
