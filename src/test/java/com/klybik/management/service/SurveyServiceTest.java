package com.klybik.management.service;

import com.klybik.management.constant.enums.EvaluationMethodEnum;
import com.klybik.management.constant.enums.SurveyStatusEnum;
import com.klybik.management.dto.survey.CreateSurveyRequest;
import com.klybik.management.dto.survey.UpdateSurveyStatusRequest;
import com.klybik.management.entity.Survey;
import com.klybik.management.exception.SurveyChangeStatusException;
import com.klybik.management.repository.SurveyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SurveyServiceTest {

    @InjectMocks
    private SurveyService surveyService;

    @Mock
    private SurveyRepository surveyRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSurveyById_shouldReturnSurvey_whenSurveyExists() {
        UUID surveyId = UUID.randomUUID();
        Survey mockSurvey = Survey.builder()
                .id(surveyId)
                .name("Test Survey")
                .description("Test Description")
                .status(SurveyStatusEnum.DRAFT)
                .evaluationMethod(EvaluationMethodEnum.METHOD_270)
                .createdAt(LocalDateTime.now())
                .build();

        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(mockSurvey));

        Survey result = surveyService.getSurveyById(surveyId);

        assertNotNull(result);
        assertEquals("Test Survey", result.getName());
        verify(surveyRepository, times(1)).findById(surveyId);
    }

    @Test
    void getSurveyById_shouldThrowException_whenSurveyNotFound() {
        UUID surveyId = UUID.randomUUID();
        when(surveyRepository.findById(surveyId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> surveyService.getSurveyById(surveyId));
        verify(surveyRepository, times(1)).findById(surveyId);
    }

    @Test
    void createSurvey_shouldSaveAndReturnSurvey() {
        CreateSurveyRequest createSurveyRequest = CreateSurveyRequest.builder()
                .name("New Survey")
                .description("New Description")
                .evaluationMethod(EvaluationMethodEnum.METHOD_360)
                .build();

        Survey mockSurvey = Survey.builder()
                .id(UUID.randomUUID())
                .name(createSurveyRequest.getName())
                .description(createSurveyRequest.getDescription())
                .evaluationMethod(createSurveyRequest.getEvaluationMethod())
                .status(SurveyStatusEnum.DRAFT)
                .createdAt(LocalDateTime.now())
                .build();

        when(surveyRepository.save(any(Survey.class))).thenReturn(mockSurvey);

        Survey result = surveyService.createSurvey(createSurveyRequest);

        assertNotNull(result);
        assertEquals("New Survey", result.getName());
        assertEquals(SurveyStatusEnum.DRAFT, result.getStatus());
        verify(surveyRepository, times(1)).save(any(Survey.class));
    }

    @Test
    void updateStatus_shouldUpdateStatus_whenValid() {
        UUID surveyId = UUID.randomUUID();
        UpdateSurveyStatusRequest updateRequest = UpdateSurveyStatusRequest.builder()
                .status(SurveyStatusEnum.PUBLISHED)
                .build();

        Survey mockSurvey = Survey.builder()
                .id(surveyId)
                .status(SurveyStatusEnum.DRAFT)
                .build();

        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(mockSurvey));
        when(surveyRepository.save(any(Survey.class))).thenReturn(mockSurvey);

        Survey result = surveyService.updateStatus(surveyId, updateRequest);

        assertNotNull(result);
        assertEquals(SurveyStatusEnum.PUBLISHED, result.getStatus());
        verify(surveyRepository, times(1)).findById(surveyId);
        verify(surveyRepository, times(1)).save(mockSurvey);
    }

    @Test
    void updateStatus_shouldThrowException_whenStatusInvalid() {
        UUID surveyId = UUID.randomUUID();
        UpdateSurveyStatusRequest updateRequest = UpdateSurveyStatusRequest.builder()
                .status(SurveyStatusEnum.DRAFT)
                .build();

        Survey mockSurvey = Survey.builder()
                .id(surveyId)
                .status(SurveyStatusEnum.PUBLISHED)
                .build();

        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(mockSurvey));

        assertThrows(SurveyChangeStatusException.class, () -> surveyService.updateStatus(surveyId, updateRequest));
        verify(surveyRepository, times(1)).findById(surveyId);
    }
}
