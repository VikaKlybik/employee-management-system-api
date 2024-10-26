package com.klybik.management.controller;

import com.klybik.management.dto.employee.EmployeeResponse;
import com.klybik.management.dto.filter.EmployeeFilterParam;
import com.klybik.management.entity.Employee;
import com.klybik.management.mapper.EmployeeMapper;
import com.klybik.management.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    @GetMapping
    public Page<EmployeeResponse> getAllEmployees(EmployeeFilterParam employeeFilterParam) {
        Page<Employee> employees = employeeService.getAllEmployee(employeeFilterParam);
        return employees.map(employeeMapper::toEmployeeResponse);
    }
}
