package com.klybik.management.mapper;

import com.klybik.management.dto.question.OneQuestionResponse;
import com.klybik.management.dto.question.QuestionResponse;
import com.klybik.management.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface QuestionMapper {
    QuestionResponse toQuestionResponse(Question question);
    @Mapping(source = "survey.id", target = "surveyId")
    OneQuestionResponse toOneQuestionResponse(Question question);
}
