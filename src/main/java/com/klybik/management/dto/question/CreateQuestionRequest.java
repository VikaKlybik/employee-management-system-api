package com.klybik.management.dto.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateQuestionRequest {
    private String name;
    private UUID competencyId;
    private UUID surveyId;
}
