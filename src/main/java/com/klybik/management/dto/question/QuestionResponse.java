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
public class QuestionResponse {
    private UUID id;
    private String name;
    private Competency competency;
}
