package com.klybik.management.dto.organization;

import com.klybik.management.dto.employee.EmployeeResponse;
import com.klybik.management.dto.jobtitle.JobTitleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationResponse {
    private UUID id;
    private String name;
    private List<JobTitleResponse> jobs;
    private EmployeeResponse head;
}
