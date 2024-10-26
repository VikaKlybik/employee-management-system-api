package com.klybik.management.dto.question;

import com.klybik.management.entity.Competency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OneQuestionResponse {
    private UUID id;
    private String name;
    private Competency competency;
    private UUID surveyId;
}
