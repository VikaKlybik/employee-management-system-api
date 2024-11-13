package com.klybik.management.dto.filter;

import com.klybik.management.constant.enums.EvaluationMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateDefaultEvaluatorsParam {
    private UUID userId;
    private EvaluationMethodEnum method;
}
