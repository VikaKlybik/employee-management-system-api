package com.klybik.management.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeUpdateRequest {
    private String firstName;
    private String lastName;
    private String email;
    private UUID jobTitleId;
}
