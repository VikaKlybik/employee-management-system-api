package com.klybik.management.service;

import com.klybik.management.dto.department.DepartmentResponse;
import com.klybik.management.dto.organization.OrganizationResponse;
import com.klybik.management.entity.Department;
import com.klybik.management.mapper.DepartmentMapper;
import com.klybik.management.mapper.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final DepartmentMapper departmentMapper;
    private final DepartmentService departmentService;
    private final EmployeeMapper employeeMapper;

    public List<OrganizationResponse> getOrganizationStructure() {
        List<Department> departments = departmentService.getAllDepartments();
        List<DepartmentResponse> departmentResponses = departmentMapper.toListOfDepartmentResponse(departments);
        return departmentResponses.stream()
                .map(departmentResponse -> OrganizationResponse.builder()
                        .id(departmentResponse.getId())
                        .name(departmentResponse.getName())
                        .jobs(departmentResponse.getJobs())
                        .head(employeeMapper.toEmployeeResponse(
                                departmentService.getHeadOfDepartment(departmentResponse.getId())
                        ))
                        .build()
                )
                .toList();
    }
}
