package com.klybik.management.controller;

import com.klybik.management.dto.evaluators.CreateEvaluatorsRequest;
import com.klybik.management.dto.filter.SurveyFilterParam;
import com.klybik.management.dto.question.CreateQuestionRequest;
import com.klybik.management.dto.question.OneQuestionResponse;
import com.klybik.management.dto.question.UpdateQuestionRequest;
import com.klybik.management.dto.survey.*;
import com.klybik.management.entity.Question;
import com.klybik.management.entity.Survey;
import com.klybik.management.mapper.QuestionMapper;
import com.klybik.management.mapper.SurveyMapper;
import com.klybik.management.service.SurveyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
@Tag(name = "Survey Controller", description = "APIs for working with Survey data")
public class SurveyController {

    private final SurveyMapper surveyMapper;
    private final QuestionMapper questionMapper;
    private final SurveyService surveyService;

    @GetMapping
    public Page<SimpleSurveyResponse> getAllSurvey(SurveyFilterParam filterParam) {
        Page<Survey> surveys = surveyService.getAllSurvey(filterParam);
        return surveys.map(surveyMapper::toSimpleSurveyResponse);
    }

    @GetMapping("/{id}")
    public SurveyResponse getSurveyById(@PathVariable UUID id) {
        Survey survey = surveyService.getSurveyById(id);
        return surveyMapper.toSurveyResponse(survey);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SimpleSurveyResponse createSurvey(@RequestBody @Valid CreateSurveyRequest createSurveyRequest) {
        Survey survey = surveyService.createSurvey(createSurveyRequest);
        return surveyMapper.toSimpleSurveyResponse(survey);
    }

    @PostMapping("/duplicate/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public SurveyResponse duplicateSurvey(@PathVariable UUID id) {
        Survey survey = surveyService.duplicateSurvey(id);
        return surveyMapper.toSurveyResponse(survey);
    }

    @PatchMapping("/update-status/{id}")
    public SurveyResponse setStatus(@PathVariable UUID id, @RequestBody @Valid UpdateSurveyStatusRequest updateSurveyStatusRequest) {
        Survey survey = surveyService.updateStatus(id, updateSurveyStatusRequest);
        return surveyMapper.toSurveyResponse(survey);
    }

    @PostMapping("/question/create")
    public OneQuestionResponse createQuestion(@RequestBody @Valid CreateQuestionRequest createQuestionRequest) {
        Question question = surveyService.createQuestion(createQuestionRequest);
        return questionMapper.toOneQuestionResponse(question);
    }

    @DeleteMapping("/question/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuestion(@PathVariable UUID id) {
        surveyService.deleteQuestion(id);
    }

    @PutMapping("/question/update/{id}")
    public OneQuestionResponse updateQuestion(@PathVariable UUID id, UpdateQuestionRequest updateQuestionRequest) {
        Question question = surveyService.updateQuestion(id, updateQuestionRequest);
        return questionMapper.toOneQuestionResponse(question);
    }

    @PostMapping("/create/composite")
    @ResponseStatus(HttpStatus.CREATED)
    public SurveyResponse compositeCreateSurvey(@RequestBody @Valid FullSurveyCreateRequest fullSurveyCreateRequest) {
        Survey survey = surveyService.compositeCreateService(fullSurveyCreateRequest);
        return surveyMapper.toSurveyResponse(survey);
    }

    @PostMapping("/evaluators")
    @ResponseStatus(HttpStatus.CREATED)
    public void setSurveyEvaluators(@RequestBody CreateEvaluatorsRequest createEvaluatorsRequest) {
        surveyService.createEvaluators(createEvaluatorsRequest);
    }

    @PostMapping("/{id}/start")
    public SurveyResponse publishSurvey(@PathVariable UUID id) {
        Survey survey = surveyService.publishSurvey(id);
        return surveyMapper.toSurveyResponse(survey);
    }
}
