package com.klybik.management.dto.kpi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KPIAssessmentResponse {
    private UUID id;
    private BigDecimal actualValue;
    private LocalDateTime assessmentDate;
    private String comments;
}
