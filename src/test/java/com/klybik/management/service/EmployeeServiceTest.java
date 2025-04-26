package com.klybik.management.service;

import com.klybik.management.constant.EmployeeManagementSystemConstant;
import com.klybik.management.constant.enums.EvaluationMethodEnum;
import com.klybik.management.dto.filter.EmployeeFilterParam;
import com.klybik.management.dto.filter.GenerateDefaultEvaluatorsParam;
import com.klybik.management.entity.Employee;
import com.klybik.management.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveEmployee_ShouldSaveEmployee() {
        Employee employee = new Employee();
        employeeService.saveEmployee(employee);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void getAllEmployee_ShouldReturnListOfEmployees() {
        EmployeeFilterParam filterParam = new EmployeeFilterParam();
        List<Employee> employees = List.of(new Employee(), new Employee());

        when(employeeRepository.findAll(any(Specification.class))).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployee(filterParam);

        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void getByUserId_ShouldReturnEmployee() {
        UUID userId = UUID.randomUUID();
        Employee employee = new Employee();
        when(employeeRepository.findByUserId(userId)).thenReturn(Optional.of(employee));

        Employee result = employeeService.getByUserId(userId);

        assertEquals(employee, result);
        verify(employeeRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getByUserId_ShouldThrowExceptionIfNotFound() {
        UUID userId = UUID.randomUUID();
        when(employeeRepository.findByUserId(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                employeeService.getByUserId(userId));

        assertEquals(EmployeeManagementSystemConstant.Error.NotFound.EMPLOYEE.formatted(userId), exception.getMessage());
        verify(employeeRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getByEmployeeId_ShouldReturnEmployee() {
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        Employee result = employeeService.getByEmployeeId(employeeId);

        assertEquals(employee, result);
        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @Test
    void getByEmployeeId_ShouldThrowExceptionIfNotFound() {
        UUID employeeId = UUID.randomUUID();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                employeeService.getByEmployeeId(employeeId));

        assertEquals(EmployeeManagementSystemConstant.Error.NotFound.EMPLOYEE.formatted(employeeId), exception.getMessage());
        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @Test
    void getAllEmployeeInListOfId_ShouldReturnEmployees() {
        List<UUID> ids = List.of(UUID.randomUUID(), UUID.randomUUID());
        List<Employee> employees = List.of(new Employee(), new Employee());
        when(employeeRepository.findAllByUserIdIn(ids)).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployeeInListOfId(ids);

        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findAllByUserIdIn(ids);
    }
}
