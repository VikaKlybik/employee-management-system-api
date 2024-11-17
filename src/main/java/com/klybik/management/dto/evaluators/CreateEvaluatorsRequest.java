package com.klybik.management.dto.evaluators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEvaluatorsRequest {
    private UUID surveyId;
    private List<PassingRequest> passing;
}
