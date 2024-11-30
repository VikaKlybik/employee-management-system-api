package com.klybik.management.controller;

import com.klybik.management.dto.filter.KPIFilterParam;
import com.klybik.management.dto.kpi.CreateKPIAssessmentRequest;
import com.klybik.management.dto.kpi.CreateKPIRequest;
import com.klybik.management.dto.kpi.KPIResponse;
import com.klybik.management.entity.KPI;
import com.klybik.management.entity.KPIPeriod;
import com.klybik.management.entity.User;
import com.klybik.management.mapper.KPIMapper;
import com.klybik.management.service.KPIService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/kpi")
@RequiredArgsConstructor
@Tag(name = "KPI Controller", description = "APIs for working with kpi data")
public class KPIController {

    private final KPIService kpiService;
    private final KPIMapper kpiMapper;

    @GetMapping("/{id}")
    public KPIResponse getKPIById(@PathVariable UUID id) {
        KPI kpi = kpiService.getKPIById(id);
        return kpiMapper.toKPIResponse(kpi);
    }

    @GetMapping("/for-employee/{employeeId}")
    public List<KPIResponse> getKPIForEmployee(@PathVariable UUID employeeId, KPIFilterParam kpiFilterParam) {
        List<KPI> kpiList = kpiService.getKPIForEmployee(employeeId, kpiFilterParam);
        return kpiMapper.toListOfKPIResponses(kpiList);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<KPIResponse> createKPI(@RequestBody @Valid List<CreateKPIRequest> requests) {
        List<KPI> kpis = kpiService.createKPIs(requests);
        return kpiMapper.toListOfKPIResponses(kpis);
    }

    @PostMapping("/assessment/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAssessmentForKPI(@AuthenticationPrincipal User user, @RequestBody @Valid CreateKPIAssessmentRequest kpiAssessmentRequest) {
        kpiService.createAssessmentForKPI(user, kpiAssessmentRequest);
    }

    @GetMapping("/kpi-period/all")
    public List<KPIPeriod> getAllKPIPeriods() {
        return kpiService.getAllKPIPeriod();
    }
}
