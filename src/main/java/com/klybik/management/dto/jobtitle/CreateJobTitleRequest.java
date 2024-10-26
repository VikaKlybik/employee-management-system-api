package com.klybik.management.dto.jobtitle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateJobTitleRequest {
    @NotBlank(message = "Name is mandatory!")
    private String name;
    @NotNull
    private Boolean isLead;
    private UUID leadId;
    @NotNull
    private UUID departmentId;
}
