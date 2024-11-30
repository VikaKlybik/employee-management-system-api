package com.klybik.management.controller;

import com.klybik.management.dto.developmentPlan.*;
import com.klybik.management.entity.DevelopmentPlan;
import com.klybik.management.mapper.DevelopmentPlanMapper;
import com.klybik.management.service.DevelopmentPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/development-plan")
public class DevelopmentPlanController {

    private final DevelopmentPlanService developmentPlanService;
    private final DevelopmentPlanMapper developmentPlanMapper;

    @PostMapping("/for-employee")
    public List<DevelopmentPlanResponse> getDevelopmentPlanForEmployee(@RequestBody DevelopmentPlanRequest developmentPlanRequest) {
        List<DevelopmentPlan> developmentPlans = developmentPlanService.getDevelopmentsPlan(developmentPlanRequest);
        return developmentPlanMapper.toDevelopmentPlanResponseList(developmentPlans);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DevelopmentPlanResponse createDevelopmentPlan(@RequestBody CreateDevelopmentPlanRequest createDevelopmentPlanRequest) {
        DevelopmentPlan developmentPlan = developmentPlanService.createDevelopmentPlan(createDevelopmentPlanRequest);
        return developmentPlanMapper.toDevelopmentPlanResponse(developmentPlan);
    }

    @PutMapping("/update-status")
    public DevelopmentPlanResponse updateStatus(@RequestBody UpdateStatusDevelopmentPlanStatusRequest planStatusRequest) {
        DevelopmentPlan developmentPlan = developmentPlanService.updateStatus(planStatusRequest);
        return developmentPlanMapper.toDevelopmentPlanResponse(developmentPlan);
    }
}
