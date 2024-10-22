package com.klybik.management.dto.department;

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
public class DepartmentResponse {
    private UUID id;
    private String name;
    private List<JobTitleResponse> jobs;
}
