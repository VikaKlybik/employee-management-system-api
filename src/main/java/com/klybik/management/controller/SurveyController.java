package com.klybik.management.controller;

import com.klybik.management.dto.assessementSummary.AssessmentStatsResponse;
import com.klybik.management.dto.assessementSummary.AssessmentSummaryRequest;
import com.klybik.management.dto.assessementSummary.HistoryAssessmentSummaryResponse;
import com.klybik.management.dto.evaluators.CreateEvaluatorsRequest;
import com.klybik.management.dto.filter.SurveyFilterParam;
import com.klybik.management.dto.question.CreateQuestionRequest;
import com.klybik.management.dto.question.OneQuestionResponse;
import com.klybik.management.dto.question.UpdateQuestionRequest;
import com.klybik.management.dto.survey.*;
import com.klybik.management.entity.Competency;
import com.klybik.management.entity.Passing;
import com.klybik.management.entity.Question;
import com.klybik.management.entity.Survey;
import com.klybik.management.mapper.AssessmentSummaryMapper;
import com.klybik.management.mapper.PassingMapper;
import com.klybik.management.mapper.QuestionMapper;
import com.klybik.management.mapper.SurveyMapper;
import com.klybik.management.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
@Tag(name = "Survey Controller", description = "APIs for working with Survey data")
public class SurveyController {

    private final SurveyMapper surveyMapper;
    private final QuestionMapper questionMapper;
    private final SurveyService surveyService;
    private final PassingMapper passingMapper;
    private final AssessmentSummaryMapper assessmentSummaryMapper;

    @GetMapping
    public List<SimpleSurveyResponse> getAllSurvey(SurveyFilterParam filterParam) {
        List<Survey> surveys = surveyService.getAllSurvey(filterParam);
        return surveyMapper.toListSimpleSurveyResponses(surveys);
    }

    @GetMapping("/{id}")
    public SurveyResponse getSurveyById(@PathVariable UUID id) {
        Survey survey = surveyService.getSurveyById(id);
        return surveyMapper.toSurveyResponse(survey);
    }

    @PostMapping("/pass/as-employee")
    @ResponseStatus(HttpStatus.CREATED)
    public void passSurvey(@RequestBody FullPassSurveyRequest fullPassSurveyRequest) {
        surveyService.passSurvey(fullPassSurveyRequest);
    }
    @GetMapping("/result/for-employee/{userId}")
    public List<SimpleSurveyResponse> getSurveyResultForEmployee(@PathVariable(name = "userId") UUID userId) {
        List<Survey> surveys = surveyService.getAllSurveyForEmployee(userId);
        return surveyMapper.toListSimpleSurveyResponses(surveys);
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

    @PostMapping("/{id}/close")
    public SurveyResponse closeSurvey(@PathVariable UUID id) {
        Survey survey = surveyService.closeSurvey(id);
        return surveyMapper.toSurveyResponse(survey);
    }

    @GetMapping("/to-be-pass/employee/{id}")
    public List<FullPassingResponse> getAllToBePassSurvey(@PathVariable UUID id) {
        List<Passing> passingList = surveyService.getAllSurveyToBePassForEmployee(id);
        return passingMapper.toListFullPassingResponse(passingList);
    }

    @GetMapping("/passing/{id}")
    public FullPassingResponse getPassingById(@PathVariable UUID id) {
        Passing passing = surveyService.getPassingById(id);
        return passingMapper.toFullPassingResponse(passing);
    }

    @PostMapping("/passing/get/assessment-summary")
    public AssessmentStatsResponse getAssessmentSummary(@RequestBody AssessmentSummaryRequest assessmentSummaryRequest) {
        return surveyService.getAssessmentSummary(assessmentSummaryRequest);
    }

    @GetMapping("/{id}/competency")
    public List<Competency> getUniqueCompetencyForSurvey(@PathVariable UUID id) {
        return surveyService.getUniqueCompetencyForSurvey(id);
    }

    @GetMapping("/passing/history/assessment-summary/for-employee/{userId}")
    public List<HistoryAssessmentSummaryResponse> getDynamicOfAssessmentSummary(@PathVariable UUID userId) {
        return surveyService.getDynamicOfAssessmentSummary(userId);
    }

    @GetMapping("/{id}/report/for-employee/{userId}")
    @Operation(
            responses = {
                    @ApiResponse(
                            content = @Content(
                                    mediaType = "application/octet-stream",
                                    schema = @Schema(type = "string", format = "binary")
                            )
                    )
            }
    )
    public ResponseEntity<byte[]> getSurveyReportForUser(@PathVariable UUID id, @PathVariable UUID userId) {
        Map.Entry<String, byte[]> reportData = surveyService.generateSurveyReport(id, userId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"%s\"".formatted(reportData.getKey()))
                .body(reportData.getValue());
    }
}
