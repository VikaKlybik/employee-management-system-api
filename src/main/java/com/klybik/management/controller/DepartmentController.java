package com.klybik.management.controller;

import com.klybik.management.dto.department.CreateDepartmentRequest;
import com.klybik.management.dto.department.DepartmentResponse;
import com.klybik.management.entity.Department;
import com.klybik.management.mapper.DepartmentMapper;
import com.klybik.management.service.DepartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
@Tag(name = "Department Controller", description = "APIs for working with department data")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    @GetMapping("/{id}")
    public DepartmentResponse getDepartment(@PathVariable UUID id) {
        Department department = departmentService.getDepartmentById(id);
        return departmentMapper.toDepartmentResponse(department);
    }

    @GetMapping
    public List<DepartmentResponse> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return departmentMapper.toListOfDepartmentResponse(departments);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentResponse createDepartment(@RequestBody @Valid CreateDepartmentRequest request) {
        Department department = departmentMapper.toDepartment(request);
        Department createdDepartment = departmentService.saveDepartment(department);
        return departmentMapper.toDepartmentResponse(createdDepartment);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDepartment(@PathVariable UUID id) {
        departmentService.deleteById(id);
    }

    @PutMapping("/{id}")
    public DepartmentResponse updateById(@PathVariable UUID id, @RequestBody @Valid CreateDepartmentRequest createDepartmentRequest) {
        Department department = departmentMapper.toDepartment(createDepartmentRequest);
        department.setId(id);
        Department updatedDepartment = departmentService.updateDepartment(department);
        return departmentMapper.toDepartmentResponse(updatedDepartment);
    }
}
