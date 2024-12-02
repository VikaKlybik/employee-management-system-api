package com.klybik.management.controller;

import com.klybik.management.service.SurveyMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final SurveyMailService surveyMailService;

    @PostMapping("/remind/passing-list/{passing-id}")
    public void remindPassingList(@PathVariable("passing-id") UUID passingId) {
        surveyMailService.sendRemindToPassSurvey(passingId);
    }

    @PostMapping("/survey/{survey-id}/published")
    public void publishSurvey(@PathVariable("survey-id") UUID surveyId) {
        surveyMailService.sendSurveyPublishedEvent(surveyId);
    }
}
