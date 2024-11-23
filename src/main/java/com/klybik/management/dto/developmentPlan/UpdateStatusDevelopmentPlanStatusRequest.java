package com.klybik.management.dto.developmentPlan;

import com.klybik.management.constant.enums.DevelopmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatusDevelopmentPlanStatusRequest {
    private DevelopmentStatus developmentStatus;
    private UUID developmentPlanId;
}
