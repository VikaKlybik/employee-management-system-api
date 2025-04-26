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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class KPIServiceTest {

    @InjectMocks
    private KPIService kpiService;

    @Mock
    private KPIRepository kpiRepository;

    @Mock
    private KPIAssessmentRepository kpiAssessmentRepository;

    @Mock
    private KPIPeriodRepository kpiPeriodRepository;

    @Mock
    private UserService userService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private ExcelReportService excelReportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetKPIForEmployee_Success() {
        UUID employeeId = UUID.randomUUID();
        KPIFilterParam filterParam = new KPIFilterParam();
        filterParam.setKpiPeriodId(UUID.randomUUID());
        List<KPI> kpis = List.of(new KPI());

        when(employeeService.getByEmployeeId(employeeId)).thenReturn(new Employee());
        when(kpiRepository.findByEmployeeIdAndKpiPeriodId(employeeId, filterParam.getKpiPeriodId()))
                .thenReturn(kpis);

        List<KPI> result = kpiService.getKPIForEmployee(employeeId, filterParam);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(employeeService, times(1)).getByEmployeeId(employeeId);
        verify(kpiRepository, times(1)).findByEmployeeIdAndKpiPeriodId(employeeId, filterParam.getKpiPeriodId());
    }

    @Test
    void testGetKPIById_WhenKPIExists() {
        UUID kpiId = UUID.randomUUID();
        KPI kpi = new KPI();
        kpi.setId(kpiId);

        when(kpiRepository.findById(kpiId)).thenReturn(Optional.of(kpi));

        KPI result = kpiService.getKPIById(kpiId);

        assertNotNull(result);
        assertEquals(kpiId, result.getId());
        verify(kpiRepository, times(1)).findById(kpiId);
    }

    @Test
    void testGetKPIById_WhenKPINotExists() {
        UUID kpiId = UUID.randomUUID();

        when(kpiRepository.findById(kpiId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> kpiService.getKPIById(kpiId));
        verify(kpiRepository, times(1)).findById(kpiId);
    }

    @Test
    void testCreateAssessmentForKPI_Success() {
        User user = new User();
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        user.setEmployee(employee);

        CreateKPIAssessmentRequest request = new CreateKPIAssessmentRequest();
        request.setKpiId(UUID.randomUUID());
        request.setActualValue(BigDecimal.valueOf(100));

        KPI kpi = new KPI();
        kpi.setId(request.getKpiId());
        kpi.setEmployee(employee);

        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        when(kpiRepository.findById(request.getKpiId())).thenReturn(Optional.of(kpi));

        assertDoesNotThrow(() -> kpiService.createAssessmentForKPI(user, request));
        verify(kpiAssessmentRepository, times(1)).save(any(KPIAssessment.class));
    }

    @Test
    void testCreateAssessmentForKPI_WhenKPINotBelongsToEmployee() {
        User user = new User();
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        user.setEmployee(employee);

        CreateKPIAssessmentRequest request = new CreateKPIAssessmentRequest();
        request.setKpiId(UUID.randomUUID());

        Employee differentEmployee = new Employee();
        differentEmployee.setId(UUID.randomUUID());
        KPI kpi = new KPI();
        kpi.setEmployee(differentEmployee);

        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        when(kpiRepository.findById(request.getKpiId())).thenReturn(Optional.of(kpi));

        assertThrows(KpiNotBelongsToEmployee.class, () -> kpiService.createAssessmentForKPI(user, request));
        verify(kpiAssessmentRepository, times(0)).save(any(KPIAssessment.class));
    }

    @Test
    void testCreateKPIs_Success() {
        CreateKPIRequest request = new CreateKPIRequest();
        request.setKpiPeriodId(UUID.randomUUID());
        request.setEmployeeId(UUID.randomUUID());
        request.setName("KPI Test");

        KPIPeriod kpiPeriod = new KPIPeriod();
        Employee employee = new Employee();

        when(kpiPeriodRepository.findById(request.getKpiPeriodId())).thenReturn(Optional.of(kpiPeriod));
        when(employeeService.getByEmployeeId(request.getEmployeeId())).thenReturn(employee);
        when(kpiRepository.existsByNameIgnoreCaseAndEmployeeId(request.getName(), employee.getId()))
                .thenReturn(false);

        List<KPI> result = kpiService.createKPIs(List.of(request));

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(kpiRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testCreateKPIs_WhenDuplicateExists() {
        CreateKPIRequest request = new CreateKPIRequest();
        request.setKpiPeriodId(UUID.randomUUID());
        request.setEmployeeId(UUID.randomUUID());
        request.setName("KPI Test");

        KPIPeriod kpiPeriod = new KPIPeriod();
        Employee employee = new Employee();

        when(kpiPeriodRepository.findById(request.getKpiPeriodId())).thenReturn(Optional.of(kpiPeriod));
        when(employeeService.getByEmployeeId(request.getEmployeeId())).thenReturn(employee);
        when(kpiRepository.existsByNameIgnoreCaseAndEmployeeId(request.getName(), employee.getId()))
                .thenReturn(true);

        assertThrows(DuplicateKeyException.class, () -> kpiService.createKPIs(List.of(request)));
        verify(kpiRepository, times(0)).saveAll(anyList());
    }
}
