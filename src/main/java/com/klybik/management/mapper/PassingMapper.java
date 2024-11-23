package com.klybik.management.mapper;

import com.klybik.management.dto.evaluators.PassingResponse;
import com.klybik.management.dto.survey.FullPassingResponse;
import com.klybik.management.entity.Passing;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {
                EmployeeMapper.class
        }
)
public interface PassingMapper {
    PassingResponse toPassingResponse(Passing passing);
    List<PassingResponse> toListPassingResponse(List<Passing> passingList);
    FullPassingResponse toFullPassingResponse(Passing passing);
}
