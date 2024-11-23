package com.klybik.management.dto.survey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassSurveyRequest {
    private UUID questionId;
    private Integer mark;
}
