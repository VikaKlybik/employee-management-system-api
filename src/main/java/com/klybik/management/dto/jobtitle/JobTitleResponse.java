package com.klybik.management.dto.jobtitle;

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
public class JobTitleResponse {
    private UUID id;
    private String name;
    private Boolean isLead;
    private SimpleJobTitleResponse lead;
    private List<SimpleJobTitleResponse> subordinates;
}
