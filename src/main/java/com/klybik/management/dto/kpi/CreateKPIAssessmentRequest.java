package com.klybik.management.dto.kpi;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateKPIAssessmentRequest {
    @NotNull(message = "ActualValue is mandatory!")
    @Min(message = "ActualValue can be less than '0'", value = 0)
    private BigDecimal actualValue;
    private String comments;
    @NotNull(message = "kpiId is mandatory!")
    private UUID kpiId;
}
