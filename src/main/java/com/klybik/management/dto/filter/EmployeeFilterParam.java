package com.klybik.management.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeFilterParam {
    private UUID jobTitleId;
    private UUID departmentId;
    private int pageNumber = 0;
    private int pageSize = 25;
}
