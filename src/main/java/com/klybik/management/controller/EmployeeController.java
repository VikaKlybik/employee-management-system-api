package com.klybik.management.controller;

import com.klybik.management.dto.employee.EmployeeResponse;
import com.klybik.management.dto.filter.EmployeeFilterParam;
import com.klybik.management.dto.filter.GenerateDefaultEvaluatorsParam;
import com.klybik.management.entity.Employee;
import com.klybik.management.mapper.EmployeeMapper;
import com.klybik.management.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    @GetMapping
    public List<EmployeeResponse> getAllEmployees(EmployeeFilterParam employeeFilterParam) {
        List<Employee> employees = employeeService.getAllEmployee(employeeFilterParam);
        return employeeMapper.toListOfEmployeeResponse(employees);
    }

    @GetMapping("/{userId}")
    public EmployeeResponse getEmployeeById(@PathVariable UUID userId) {
        Employee employee = employeeService.getByUserId(userId);
        return employeeMapper.toEmployeeResponse(employee);
    }

    @GetMapping("/evaluators/generate")
    public List<EmployeeResponse> generateDefaultEvaluators(GenerateDefaultEvaluatorsParam generateDefaultEvaluators) {
        List<Employee> employees = employeeService.generateDefaultEvaluators(generateDefaultEvaluators);
        return employeeMapper.toListOfEmployeeResponse(employees);
    }

}
