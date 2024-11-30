package com.klybik.management.service;

import com.klybik.management.dto.filter.KPIFilterParam;
import com.klybik.management.dto.kpi.CreateKPIAssessmentRequest;
import com.klybik.management.dto.kpi.CreateKPIRequest;
import com.klybik.management.entity.*;
import com.klybik.management.exception.KpiNotBelongsToEmployee;
import com.klybik.management.repository.KPIAssessmentRepository;
import com.klybik.management.repository.KPIPeriodRepository;
import com.klybik.management.repository.KPIRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.NotFound;
import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.Logic;
import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.Duplicate;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KPIService {

    private final KPIRepository kpiRepository;
    private final KPIAssessmentRepository kpiAssessmentRepository;
    private final UserService userService;
    private final KPIPeriodRepository kpiPeriodRepository;
    private final EmployeeService employeeService;

    public List<KPI> getKPIForEmployee(UUID employeeId, KPIFilterParam kpiFilterParam) {
        return kpiRepository.findByEmployeeIdAndKpiPeriodId(employeeId, kpiFilterParam.getKpiPeriodId());
    }

    public KPI getKPIById(UUID id) {
        return kpiRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NotFound.KPI.formatted(id)));
    }

    @Transactional
    public void createAssessmentForKPI(User user, CreateKPIAssessmentRequest kpiAssessmentRequest) {
        User existingUser = userService.getUserByEmail(user.getEmail());
        Employee employee = existingUser.getEmployee();
        KPI kpi = getKPIById(kpiAssessmentRequest.getKpiId());

        if(!kpi.getEmployee().getId().equals(employee.getId())) {
            throw new KpiNotBelongsToEmployee(Logic.KPI_NOT_BELONGS_TO_EMPLOYEE);
        }

        KPIAssessment kpiAssessment = KPIAssessment.builder()
                .actualValue(kpiAssessmentRequest.getActualValue())
                .assessmentDate(LocalDateTime.now())
                .comments(kpiAssessmentRequest.getComments())
                .kpi(kpi)
                .build();
        kpiAssessmentRepository.save(kpiAssessment);
    }

    public List<KPI> createKPIs(@Valid List<CreateKPIRequest> requests) {
        List<KPI> kpiList = new ArrayList<>();
        for(CreateKPIRequest request: requests) {
            KPIPeriod kpiPeriod = kpiPeriodRepository.findById(request.getKpiPeriodId())
                    .orElseThrow(() -> new EntityNotFoundException(NotFound.KPI_PERIOD.formatted(request.getKpiPeriodId())));
            Employee employee = employeeService.getByEmployeeId(request.getEmployeeId());

            if (kpiRepository.existsByNameIgnoreCaseAndEmployeeId(request.getName(), employee.getId())) {
                throw new DuplicateKeyException(Duplicate.KPI.formatted(request.getName()));
            }
            KPI kpi = KPI.builder()
                    .kpiPeriod(kpiPeriod)
                    .description(request.getDescription())
                    .name(request.getName())
                    .employee(employee)
                    .measureUnit(request.getMeasureUnit())
                    .targetValue(request.getTargetValue())
                    .weight(request.getWeight())
                    .build();
            kpiList.add(kpi);
        }
        return kpiRepository.saveAll(kpiList);
    }

    public List<KPIPeriod> getAllKPIPeriod() {
        return kpiPeriodRepository.findAll();
    }
}
