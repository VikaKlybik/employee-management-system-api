package com.klybik.management.service;

import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.NotFound;
import com.klybik.management.dto.question.CreateQuestionRequest;
import com.klybik.management.dto.question.UpdateQuestionRequest;
import com.klybik.management.entity.Competency;
import com.klybik.management.entity.Question;
import com.klybik.management.entity.Survey;
import com.klybik.management.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final SurveyService surveyService;
    private final CompetencyService competencyService;

    public Question createQuestion(CreateQuestionRequest createQuestionRequest) {
        Survey survey = surveyService.getSurveyById(createQuestionRequest.getSurveyId());
        Competency competency = competencyService.getCompetencyById(createQuestionRequest.getCompetencyId());

        surveyService.validateAbilityToEditSurvey(survey);

        return questionRepository.save(Question.builder()
                .competency(competency)
                .name(createQuestionRequest.getName())
                .survey(survey)
                .build());
    }

    public void deleteQuestion(UUID id) {
        Question question = getQuestionById(id);
        Survey survey = question.getSurvey();
        surveyService.validateAbilityToEditSurvey(survey);
        questionRepository.delete(question);
    }

    Question getQuestionById(UUID questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException(NotFound.SURVEY.formatted(questionId)));
    }

    public Question updateQuestion(UUID id, UpdateQuestionRequest updateQuestionRequest) {
        Question question = getQuestionById(id);
        Survey survey = question.getSurvey();
        surveyService.validateAbilityToEditSurvey(survey);
        Competency competency = competencyService.getCompetencyById(updateQuestionRequest.getCompetencyId());

        question.setName(updateQuestionRequest.getName());
        question.setCompetency(competency);
        return questionRepository.save(question);
    }
}
