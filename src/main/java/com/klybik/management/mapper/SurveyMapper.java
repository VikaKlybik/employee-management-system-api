package com.klybik.management.mapper;

import com.klybik.management.dto.survey.SimpleSurveyResponse;
import com.klybik.management.dto.survey.SurveyResponse;
import com.klybik.management.entity.Survey;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = QuestionMapper.class)
public interface SurveyMapper {
    SimpleSurveyResponse toSimpleSurveyResponse(Survey survey);
    SurveyResponse toSurveyResponse(Survey survey);
}
