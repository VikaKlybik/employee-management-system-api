package com.klybik.management.dto.evaluators;

import com.klybik.management.dto.employee.EmployeeResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassingResponse {
    private UUID id;
    private Boolean isPass;
    private EmployeeResponse evaluatedPerson;
    private EmployeeResponse evaluator;
}
