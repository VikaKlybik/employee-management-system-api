package com.klybik.management.mapper;

import com.klybik.management.dto.assessementSummary.AssessmentSummaryResponse;
import com.klybik.management.dto.developmentPlan.DevelopmentPlanResponse;
import com.klybik.management.entity.AssessmentSummary;
import com.klybik.management.entity.DevelopmentPlan;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {
                SurveyMapper.class,
        }
)
public interface DevelopmentPlanMapper {
    DevelopmentPlanResponse toDevelopmentPlanResponse(DevelopmentPlan developmentPlan);
    List<DevelopmentPlanResponse> toDevelopmentPlanResponseList(List<DevelopmentPlan> developmentPlans);
}
