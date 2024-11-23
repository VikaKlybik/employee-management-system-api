package com.klybik.management.service;

import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.Logic;
import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.NotFound;
import com.klybik.management.constant.enums.EvaluationMethodEnum;
import com.klybik.management.constant.enums.SurveyStatusEnum;
import com.klybik.management.dto.assessementSummary.AssessmentSummaryRequest;
import com.klybik.management.dto.evaluators.CreateEvaluatorsRequest;
import com.klybik.management.dto.evaluators.PassingRequest;
import com.klybik.management.dto.filter.SurveyFilterParam;
import com.klybik.management.dto.question.CreateQuestionRequest;
import com.klybik.management.dto.question.UpdateQuestionRequest;
import com.klybik.management.dto.survey.CreateSurveyRequest;
import com.klybik.management.dto.survey.FullPassSurveyRequest;
import com.klybik.management.dto.survey.FullSurveyCreateRequest;
import com.klybik.management.dto.survey.UpdateSurveyStatusRequest;
import com.klybik.management.entity.*;
import com.klybik.management.exception.SurveyChangeStatusException;
import com.klybik.management.exception.handler.LogicDataException;
import com.klybik.management.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final CompetencyService competencyService;
    private final EmployeeService employeeService;
    private final PassingRepository passingRepository;
    private final ResponseRepository responseRepository;
    private final AssessmentSummaryRepository assessmentSummaryRepository;

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

    public Question getQuestionById(UUID questionId) {
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

    @Transactional
    public void createEvaluators(CreateEvaluatorsRequest createEvaluatorsRequest) {
        Survey survey = getSurveyById(createEvaluatorsRequest.getSurveyId());

        for (PassingRequest passingRequest : createEvaluatorsRequest.getPassing()) {
            Employee evaluated = employeeService.getByUserId(passingRequest.getEvaluatedId());
            List<Employee> evaluators = employeeService.getAllEmployeeInListOfId(passingRequest.getEvaluatorIds());
            List<Passing> passingList = evaluators.stream()
                    .map(evaluator -> Passing.builder()
                            .isPass(Boolean.FALSE)
                            .evaluatedPerson(evaluated)
                            .evaluator(evaluator)
                            .survey(survey)
                            .build())
                    .toList();
            passingRepository.saveAll(passingList);
        }
        survey.setStatus(SurveyStatusEnum.PUBLISHED);
        surveyRepository.save(survey);
    }

    public Survey publishSurvey(UUID id) {
        Survey survey = getSurveyById(id);
        if (survey.getStatus() != SurveyStatusEnum.DRAFT) {
            throw new SurveyChangeStatusException(Logic.SURVEY_PUBLISHED);
        }
        survey.setStatus(SurveyStatusEnum.PUBLISHED);
        // add logic with notification to user
        return surveyRepository.save(survey);
    }

    public List<Passing> getAllSurveyToBePassForEmployee(UUID id) {
        return passingRepository.findAllByEvaluatorUserIdAndIsPassFalse(id);
    }

    @Transactional
    public void passSurvey(FullPassSurveyRequest listOfPassSurveyRequest) {
        Passing passing = passingRepository.findById(listOfPassSurveyRequest.getPassingId())
                .orElseThrow(() -> new EntityNotFoundException(NotFound.SURVEY.formatted(listOfPassSurveyRequest.getPassingId())));

        List<Response> responses = listOfPassSurveyRequest
                .getResponses()
                .stream()
                .map(response ->
                        Response.builder()
                                .question(getQuestionById(response.getQuestionId()))
                                .mark(response.getMark())
                                .evaluatedEmployee(passing.getEvaluatedPerson())
                                .isSelfAssessment(passing.getEvaluatedPerson().getId().equals(passing.getEvaluator().getId()))
                                .build()
                )
                .toList();
        passing.setIsPass(Boolean.TRUE);
        passingRepository.save(passing);
        responseRepository.saveAll(responses);
    }

    public Passing getPassingById(UUID id) {
        return passingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NotFound.SURVEY.formatted(id)));
    }

    @Transactional
    public Survey closeSurvey(UUID id) {
        //TODO logic for creating devolpement plan

        Survey survey = getSurveyById(id);

        List<AssessmentSummary> assessmentSummaryList = survey.getQuestions().stream()
                .flatMap(question -> question.getResponses().stream()
                        .collect(Collectors.groupingBy(Response::getEvaluatedEmployee))
                        .entrySet().stream()
                        .map(entry -> {
                            Employee evaluatedEmployee = entry.getKey();
                            List<Response> responses = entry.getValue();

                            BigDecimal assessmentSummary = responses.stream()
                                    .map(response -> BigDecimal.valueOf(response.getMark()))
                                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                                    .divide(new BigDecimal(responses.size()), 2, RoundingMode.HALF_UP);

                            return AssessmentSummary.builder()
                                    .assessmentSummary(assessmentSummary)
                                    .totalReviews(responses.size())
                                    .survey(survey)
                                    .employee(evaluatedEmployee)
                                    .competency(question.getCompetency())
                                    .build();
                        }))
                .collect(Collectors.toList());

        assessmentSummaryRepository.saveAll(assessmentSummaryList);

        survey.setStatus(SurveyStatusEnum.CLOSED);
        return surveyRepository.save(survey);
    }

    public List<AssessmentSummary> getAssessmentSummary(AssessmentSummaryRequest assessmentSummaryRequest) {
        return assessmentSummaryRepository.findByEmployeeUserIdAndSurveyId(
                assessmentSummaryRequest.getUserId(),
                assessmentSummaryRequest.getSurveyId()
        );
    }
}
