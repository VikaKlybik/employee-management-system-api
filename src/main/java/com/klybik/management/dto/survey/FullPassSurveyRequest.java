package com.klybik.management.dto.survey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullPassSurveyRequest {
    private UUID passingId;
    private List<PassSurveyRequest> responses;
}
