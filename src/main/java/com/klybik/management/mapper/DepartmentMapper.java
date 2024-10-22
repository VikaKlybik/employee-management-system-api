package com.klybik.management.mapper;

import com.klybik.management.dto.department.CreateDepartmentRequest;
import com.klybik.management.dto.department.DepartmentResponse;
import com.klybik.management.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = JobTitleMapper.class)
public interface DepartmentMapper {
    DepartmentResponse toDepartmentResponse(Department department);
    List<DepartmentResponse> toListOfDepartmentResponse(List<Department> departments);
    Department toDepartment(CreateDepartmentRequest createDepartmentRequest);
}
