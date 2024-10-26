package com.klybik.management.mapper;

import com.klybik.management.dto.employee.EmployeeResponse;
import com.klybik.management.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {
                UserMapper.class,
                JobTitleMapper.class,
                DepartmentMapper.class
        }
)
public interface EmployeeMapper {
    @Mapping(source = "jobTitle.department", target = "department")
    EmployeeResponse toEmployeeResponse(Employee employee);
}
