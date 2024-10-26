package com.klybik.management.dto.question;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateQuestionRequest {
    @NotNull(message = "Name is mandatory!")
    private String name;
    @NotNull(message = "CompetencyId is mandatory!")
    private UUID competencyId;
}
