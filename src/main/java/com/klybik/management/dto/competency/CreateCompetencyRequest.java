package com.klybik.management.dto.competency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCompetencyRequest {
    private String name;
    private String description;
}
