package com.klybik.management.dto.assessementSummary;

import com.klybik.management.constant.enums.StatisticTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentSummaryRequest {
    private UUID userId;
    private UUID surveyId;
    private StatisticTypeEnum statisticType;
}
