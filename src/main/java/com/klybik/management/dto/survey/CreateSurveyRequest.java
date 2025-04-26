package com.klybik.management.dto.survey;

import com.klybik.management.constant.enums.EvaluationMethodEnum;
import com.klybik.management.constant.enums.SurveyStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateSurveyRequest {
    @NotNull(message = "Name is mandatory!")
    private String name;
    @NotNull(message = "Description is mandatory!")
    private String description;
    @NotNull(message = "EvaluationMethod is mandatory!")
    private EvaluationMethodEnum evaluationMethod;
}
