package com.klybik.management.dto.kpi;

import com.klybik.management.constant.enums.MeasureUnitEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KPIResponse {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal targetValue;
    private MeasureUnitEnum measureUnit;
    private BigDecimal weight;
    private UUID employeeId;
    private List<KPIAssessmentResponse> kpiAssessments;
}
