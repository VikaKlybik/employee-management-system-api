package com.klybik.management.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompositeCreateQuestionRequest {
    private String name;
    private UUID competencyId;
}
