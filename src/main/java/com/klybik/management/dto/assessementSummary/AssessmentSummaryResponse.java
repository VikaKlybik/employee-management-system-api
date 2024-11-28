package com.klybik.management.dto.assessementSummary;

import com.klybik.management.constant.enums.EvaluatorTypeEnum;
import com.klybik.management.entity.Competency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentSummaryResponse {
    private UUID id;
    private BigDecimal assessmentSummary;
    private Integer totalReviews;
    private Competency competency;
    private EvaluatorTypeEnum evaluatorType;
}
