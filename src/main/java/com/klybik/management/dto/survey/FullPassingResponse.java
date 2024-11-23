package com.klybik.management.dto.survey;

import com.klybik.management.dto.employee.EmployeeResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullPassingResponse {
    private UUID id;
    private Boolean isPass;
    private EmployeeResponse evaluatedPerson;
    private EmployeeResponse evaluator;
    private SurveyWithoutPassingResponse survey;
}
