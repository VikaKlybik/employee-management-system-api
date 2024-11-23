package com.klybik.management.mapper;

import com.klybik.management.dto.assessementSummary.AssessmentSummaryResponse;
import com.klybik.management.entity.AssessmentSummary;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {
                EmployeeMapper.class,
        }
)
public interface AssessmentSummaryMapper {
    List<AssessmentSummaryResponse> toAssessmentSummaryResponseList(List<AssessmentSummary> assessmentSummaries);
}
