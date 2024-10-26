package com.klybik.management.dto.filter;

import com.klybik.management.constant.enums.EvaluationMethodEnum;
import com.klybik.management.constant.enums.SurveyStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurveyFilterParam {
    private SurveyStatusEnum status;
    private EvaluationMethodEnum evaluationMethod;
    private int pageNumber = 0;
    private int pageSize = 10;
    private String direction = "asc";
}