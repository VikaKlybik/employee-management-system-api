package com.klybik.management.controller;

import com.klybik.management.dto.department.CreateDepartmentRequest;
import com.klybik.management.dto.department.DepartmentResponse;
import com.klybik.management.dto.organization.OrganizationResponse;
import com.klybik.management.entity.Department;
import com.klybik.management.mapper.DepartmentMapper;
import com.klybik.management.service.DepartmentService;
import com.klybik.management.service.OrganizationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/organization")
@RequiredArgsConstructor
@Tag(name = "Organization Controller", description = "APIs for working with organization data")
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping
    public List<OrganizationResponse> getOrganizationStructure() {
        return organizationService.getOrganizationStructure();
    }

}
