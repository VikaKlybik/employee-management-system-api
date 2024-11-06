package com.klybik.management.dto.employee;

import com.klybik.management.dto.department.SimpleDepartmentResponse;
import com.klybik.management.dto.jobtitle.JobTitleResponse;
import com.klybik.management.dto.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeResponse {
    private UUID id;
    private LocalDateTime workSince;
    private UserResponse user;
    private JobTitleResponse jobTitle;
    private SimpleDepartmentResponse department;
}
