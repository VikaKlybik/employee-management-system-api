package com.klybik.management.service;

import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.Logic;
import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.NotFound;
import com.klybik.management.constant.enums.EvaluationMethodEnum;
import com.klybik.management.constant.enums.SurveyStatusEnum;
import com.klybik.management.dto.filter.SurveyFilterParam;
import com.klybik.management.dto.question.CreateQuestionRequest;
import com.klybik.management.dto.question.UpdateQuestionRequest;
import com.klybik.management.dto.survey.CreateSurveyRequest;
import com.klybik.management.dto.survey.FullSurveyCreateRequest;
import com.klybik.management.dto.survey.UpdateSurveyStatusRequest;
import com.klybik.management.entity.Competency;
import com.klybik.management.entity.Question;
import com.klybik.management.entity.Survey;
import com.klybik.management.exception.SurveyChangeStatusException;
import com.klybik.management.exception.handler.LogicDataException;
import com.klybik.management.repository.QuestionRepository;
import com.klybik.management.repository.SurveyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final CompetencyService competencyService;

    public Page<Survey> getAllSurvey(SurveyFilterParam filterParam) {
        Specification<Survey> surveySpecification = Specification
                .where(hasStatus(filterParam.getStatus()))
                .and(hasEvaluationMethod(filterParam.getEvaluationMethod()));

        Sort sort = Sort.by(Sort.Direction.fromString(filterParam.getDirection()), "createdAt");
        Pageable pageable = PageRequest.of(filterParam.getPageNumber(), filterParam.getPageSize(), sort);
        return surveyRepository.findAll(surveySpecification, pageable);
    }


    private Specification<Survey> hasStatus(SurveyStatusEnum surveyStatus) {
        return ((root, query, criteriaBuilder) -> {
            if (surveyStatus == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), surveyStatus.ordinal());
        });
    }

    private Specification<Survey> hasEvaluationMethod(EvaluationMethodEnum evaluationMethod) {
        return ((root, query, criteriaBuilder) -> {
            if (evaluationMethod == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("evaluationMethod"), evaluationMethod.ordinal());
        });
    }

    public Survey getSurveyById(UUID id) {
        return surveyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NotFound.SURVEY.formatted(id)));
    }

    public Survey updateStatus(UUID id, UpdateSurveyStatusRequest updateSurveyStatusRequest) {
        Survey survey = getSurveyById(id);
        //statuses equal
        if (survey.getStatus() == updateSurveyStatusRequest.getStatus()) {
            return survey;
        }
        //survey.
        if (survey.getStatus() == SurveyStatusEnum.CLOSED) {
            throw new SurveyChangeStatusException(Logic.SURVEY_CLOSED);
        }

        if (survey.getStatus() == SurveyStatusEnum.PUBLISHED && updateSurveyStatusRequest.getStatus() == SurveyStatusEnum.DRAFT) {
            throw new SurveyChangeStatusException(Logic.SURVEY_PUBLISHED);
        }
        survey.setStatus(updateSurveyStatusRequest.getStatus());
        return surveyRepository.save(survey);
    }

    public Survey createSurvey(CreateSurveyRequest createSurveyRequest) {
        Survey survey = Survey.builder()
                .name(createSurveyRequest.getName())
                .description(createSurveyRequest.getDescription())
                .createdAt(LocalDateTime.now())
                .evaluationMethod(createSurveyRequest.getEvaluationMethod())
                .status(SurveyStatusEnum.DRAFT)
                .build();
        return surveyRepository.save(survey);
    }

    void validateAbilityToEditSurvey(Survey survey) {
        if (survey.getStatus() != SurveyStatusEnum.DRAFT) {
            throw new LogicDataException(Logic.INVALID_SURVEY_STATUS.formatted(survey.getStatus().name()));
        }
    }

    @Transactional
    public Survey duplicateSurvey(UUID id) {
        Survey survey = getSurveyById(id);

        Survey newSurvey = Survey.builder()
                .name(survey.getName())
                .description(survey.getDescription())
                .createdAt(LocalDateTime.now())
                .evaluationMethod(survey.getEvaluationMethod())
                .status(SurveyStatusEnum.DRAFT)
                .build();

        List<Question> duplicateQuestions = survey.getQuestions()
                .stream()
                .map(question -> Question.builder()
                        .competency(question.getCompetency())
                        .survey(newSurvey)
                        .name(question.getName())
                        .build())
                .toList();

        newSurvey.setQuestions(duplicateQuestions);
        return surveyRepository.save(newSurvey);
    }

    @Transactional
    public Survey compositeCreateService(FullSurveyCreateRequest fullSurveyCreateRequest) {
        Survey createdSurvey = surveyRepository.save(Survey.builder()
                .name(fullSurveyCreateRequest.getName())
                .description(fullSurveyCreateRequest.getDescription())
                .status(SurveyStatusEnum.DRAFT)
                .createdAt(LocalDateTime.now())
                .evaluationMethod(fullSurveyCreateRequest.getEvaluationMethod())
                .build());

        fullSurveyCreateRequest.getQuestions().forEach(question ->
                createQuestion(CreateQuestionRequest.builder()
                        .surveyId(createdSurvey.getId())
                        .competencyId(question.getCompetencyId())
                        .name(question.getName())
                        .build())
        );
        return getSurveyById(createdSurvey.getId());
    }

    @Transactional
    public Question createQuestion(CreateQuestionRequest createQuestionRequest) {
        Survey survey = getSurveyById(createQuestionRequest.getSurveyId());
        Competency competency = competencyService.getCompetencyById(createQuestionRequest.getCompetencyId());

        validateAbilityToEditSurvey(survey);

        return questionRepository.save(Question.builder()
                .competency(competency)
                .name(createQuestionRequest.getName())
                .survey(survey)
                .build());
    }

    public void deleteQuestion(UUID id) {
        Question question = getQuestionById(id);
        Survey survey = question.getSurvey();
        validateAbilityToEditSurvey(survey);
        questionRepository.delete(question);
    }

    Question getQuestionById(UUID questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException(NotFound.SURVEY.formatted(questionId)));
    }

    public Question updateQuestion(UUID id, UpdateQuestionRequest updateQuestionRequest) {
        Question question = getQuestionById(id);
        Survey survey = question.getSurvey();
        validateAbilityToEditSurvey(survey);
        Competency competency = competencyService.getCompetencyById(updateQuestionRequest.getCompetencyId());

        question.setName(updateQuestionRequest.getName());
        question.setCompetency(competency);
        return questionRepository.save(question);
    }
}
