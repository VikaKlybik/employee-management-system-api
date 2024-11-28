package com.klybik.management.dto.assessementSummary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Builder
public class AssessmentStatsAsTableResponse extends AssessmentStatsResponse {
    private List<AssessmentSummaryResponse> assessmentsSelf;
    private List<AssessmentSummaryResponse> assessmentsSubordinate;
    private List<AssessmentSummaryResponse> assessmentsColleague;
    private List<AssessmentSummaryResponse> assessmentsLead;
}

