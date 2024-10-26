package com.klybik.management.controller;

import com.klybik.management.dto.filter.SurveyFilterParam;
import com.klybik.management.dto.question.CreateQuestionRequest;
import com.klybik.management.dto.question.OneQuestionResponse;
import com.klybik.management.dto.question.UpdateQuestionRequest;
import com.klybik.management.dto.survey.CreateSurveyRequest;
import com.klybik.management.dto.survey.SimpleSurveyResponse;
import com.klybik.management.dto.survey.SurveyResponse;
import com.klybik.management.dto.survey.UpdateSurveyStatusRequest;
import com.klybik.management.entity.Question;
import com.klybik.management.entity.Survey;
import com.klybik.management.mapper.QuestionMapper;
import com.klybik.management.mapper.SurveyMapper;
import com.klybik.management.service.QuestionService;
import com.klybik.management.service.SurveyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/survey")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyMapper surveyMapper;
    private final QuestionMapper questionMapper;
    private final SurveyService surveyService;
    private final QuestionService questionService;

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
        Question question = questionService.createQuestion(createQuestionRequest);
        return questionMapper.toOneQuestionResponse(question);
    }

    @DeleteMapping("/question/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuestion(@PathVariable UUID id) {
        questionService.deleteQuestion(id);
    }

    @PutMapping("/question/update/{id}")
    public OneQuestionResponse updateQuestion(@PathVariable UUID id, UpdateQuestionRequest updateQuestionRequest) {
        Question question = questionService.updateQuestion(id, updateQuestionRequest);
        return questionMapper.toOneQuestionResponse(question);
    }
}
