package com.klybik.management.service;

import com.klybik.management.constant.enums.EvaluatorTypeEnum;
import com.klybik.management.dto.mail.EmailDetails;
import com.klybik.management.entity.Employee;
import com.klybik.management.entity.Passing;
import com.klybik.management.entity.Survey;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyMailService {

    private final NotificationService notificationService;
    private final SurveyService surveyService;
    private final SpringTemplateEngine templateEngine;
    private static final String REMIND_TO_PASS_SURVEY_TEMPLATE = "remind-to-pass-survey.html";
    private static final String SURVEY_PUBLISHED_TEMPLATE = "survey-creating.html";

    public void sendRemindToPassSurvey(UUID passingId) {
        Passing passing = surveyService.getPassingById(passingId);
        String recipient = passing.getEvaluator().getUser().getEmail();
        String content = getHtmlContent(generateProperty(passing), REMIND_TO_PASS_SURVEY_TEMPLATE);
        EmailDetails emailDetails = EmailDetails.builder()
                .subject("Напоминание о необходимости пройти опрос")
                .recipient(recipient)
                .messageBody(content)
                .build();
        notificationService.sendEmail(emailDetails);
    }

    public void sendSurveyPublishedEvent(UUID surveyId) {
        Survey survey = surveyService.getSurveyById(surveyId);
        var surveyEvaluator = survey.getPassingList()
                .stream()
                .collect(Collectors.groupingBy(Passing::getEvaluator));
        for(Employee evaluator: surveyEvaluator.keySet()) {
            try {
                String recipient = evaluator.getUser().getEmail();
                String content = getHtmlContent(generateProperty(evaluator, surveyEvaluator.get(evaluator)), SURVEY_PUBLISHED_TEMPLATE);
                EmailDetails emailDetails = EmailDetails.builder()
                        .subject("Создан новый опрос!")
                        .recipient(recipient)
                        .messageBody(content)
                        .build();
                notificationService.sendEmail(emailDetails);
            } catch (Exception e) {
                continue;
            }
        }
    }

    private String getHtmlContent(Map<String, Object> properties, String templateName) {
        Context context = new Context();
        context.setVariables(properties);
        return templateEngine.process(templateName, context);
    }

    private String generateLinkToSurvey(UUID passingId) {
        return "http://localhost:3000/survey/" + passingId;
    }

    private Map<String, Object> generateProperty(Passing passing) {
        Map<String, Object> properties = new HashMap<>();
        Employee evaluator = passing.getEvaluator();
        properties.put("username", evaluator.getUser().getFirstName() + " " + evaluator.getUser().getLastName());
        properties.put("surveyLink", generateLinkToSurvey(passing.getId()));

        Map<String, Object> colleagueData = new HashMap<>();
        Employee evaluatedPerson = passing.getEvaluatedPerson();
        colleagueData.put("firstName", evaluatedPerson.getUser().getFirstName());
        colleagueData.put("lastName", evaluatedPerson.getUser().getLastName());
        colleagueData.put("department", evaluatedPerson.getJobTitle().getDepartment().getName());
        colleagueData.put("position", evaluatedPerson.getJobTitle().getName());

        properties.put("colleague", colleagueData);
        return properties;
    }

    private Map<String, Object> generateProperty(Employee evaluator, List<Passing> passingList) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("username", evaluator.getUser().getFirstName() + " " + evaluator.getUser().getLastName());

        List<Map<String, Object>> colleagues = new ArrayList<>();
        for(Passing passing: passingList) {
            Map<String, Object> colleague = new HashMap<>();
            Employee evaluatedPerson = passing.getEvaluatedPerson();
            colleague.put("fullName", evaluatedPerson.getUser().getFirstName()+ " " + evaluatedPerson.getUser().getLastName());
            colleague.put("position", evaluatedPerson.getJobTitle().getDepartment().getName() + ", "+ evaluatedPerson.getJobTitle().getName());
            colleague.put("evaluatorType", getRussianEvaluatorType(passing.getEvaluatorType()));
            colleague.put("link", generateLinkToSurvey(passing.getId()));
            colleagues.add(colleague);
        }
        properties.put("colleagues", colleagues);
        return properties;
    }

    private String getRussianEvaluatorType(EvaluatorTypeEnum evaluatorTypeEnum) {
        return switch (evaluatorTypeEnum){
            case SELF -> "Самооценка";
            case LEAD -> "Руководитель";
            case COLLEAGUE -> "Коллега";
            case SUBORDINATE -> "Подчинённый";
        };
    }
}
