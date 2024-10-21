package com.klybik.management.mapper;

import com.klybik.management.dto.jobtitle.JobTitleResponse;
import com.klybik.management.entity.JobTitle;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface JobTitleMapper {
    JobTitleResponse toJobTitleResponse(JobTitle jobTitle);

    List<JobTitleResponse> toListOfJobTitleResponse(List<JobTitle> jobTitleList);
}
