package com.klybik.management.mapper;

import com.klybik.management.dto.kpi.KPIResponse;
import com.klybik.management.entity.KPI;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface KPIMapper {
    List<KPIResponse> toListOfKPIResponses(List<KPI> kpiList);
    @Mapping(source = "employee.id", target = "employeeId")
    KPIResponse toKPIResponse(KPI kpi);
}
