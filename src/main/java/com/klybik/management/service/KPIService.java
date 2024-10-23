package com.klybik.management.service;

import com.klybik.management.dto.kpi.CreateKPIAssessmentRequest;
import com.klybik.management.entity.Employee;
import com.klybik.management.entity.KPI;
import com.klybik.management.entity.KPIAssessment;
import com.klybik.management.entity.User;
import com.klybik.management.exception.KpiNotBelongsToEmployee;
import com.klybik.management.repository.KPIAssessmentRepository;
import com.klybik.management.repository.KPIRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.NotFound;
import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.Logic;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KPIService {

    private final KPIRepository kpiRepository;
    private final KPIAssessmentRepository kpiAssessmentRepository;
    private final UserService userService;

    public List<KPI> getKPIForEmployee(UUID employeeId) {
        return kpiRepository.findByEmployeeId(employeeId);
    }

    public KPI getKPIById(UUID id) {
        return kpiRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NotFound.KPI.formatted(id)));
    }


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
}
