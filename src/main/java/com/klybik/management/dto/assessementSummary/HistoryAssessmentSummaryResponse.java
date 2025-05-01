package com.klybik.management.dto.assessementSummary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryAssessmentSummaryResponse {
    private LocalDate date;
    private List<AssessmentSummaryResponse> assessments;
}
