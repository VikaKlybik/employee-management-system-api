package com.klybik.management.service;

import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.NotFound;
import com.klybik.management.constant.enums.DevelopmentStatus;
import com.klybik.management.dto.developmentPlan.CreateDevelopmentPlanRequest;
import com.klybik.management.dto.developmentPlan.DevelopmentPlanRequest;
import com.klybik.management.dto.developmentPlan.UpdateStatusDevelopmentPlanStatusRequest;
import com.klybik.management.entity.Competency;
import com.klybik.management.entity.DevelopmentPlan;
import com.klybik.management.entity.Employee;
import com.klybik.management.entity.Survey;
import com.klybik.management.repository.DevelopmentPlanRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DevelopmentPlanService {

    private final DevelopmentPlanRepository developmentPlanRepository;
    private final SurveyService surveyService;
    private final EmployeeService employeeService;
    private final CompetencyService competencyService;

    public List<DevelopmentPlan> getDevelopmentsPlan(DevelopmentPlanRequest developmentPlanRequest) {
        return developmentPlanRepository.findAllByEmployeeUserIdAndSurveyId(
                developmentPlanRequest.getUserId(),
                developmentPlanRequest.getSurveyId()
        );
    }

    public DevelopmentPlan createDevelopmentPlan(CreateDevelopmentPlanRequest createDevelopmentPlanRequest) {
        Survey survey = surveyService.getSurveyById(createDevelopmentPlanRequest.getSurveyId());
        Competency competency = competencyService.getCompetencyById(createDevelopmentPlanRequest.getCompetencyId());
        Employee employee = employeeService.getByUserId(createDevelopmentPlanRequest.getEmployeeUserId());
        return developmentPlanRepository.save(DevelopmentPlan.builder()
                .goal(createDevelopmentPlanRequest.getGoal())
                .comments(createDevelopmentPlanRequest.getComments())
                .employee(employee)
                .survey(survey)
                .competency(competency)
                .status(DevelopmentStatus.STARTED)
                .build());
    }

    public DevelopmentPlan updateStatus(UpdateStatusDevelopmentPlanStatusRequest planStatusRequest) {
        DevelopmentPlan developmentPlan = developmentPlanRepository.findById(planStatusRequest.getDevelopmentPlanId())
                .orElseThrow(() -> new EntityNotFoundException(NotFound.DEVELOPMENT_PLAN.formatted(
                        planStatusRequest.getDevelopmentPlanId()))
                );
        developmentPlan.setStatus(developmentPlan.getStatus());
        return developmentPlanRepository.save(developmentPlan);
    }
}
