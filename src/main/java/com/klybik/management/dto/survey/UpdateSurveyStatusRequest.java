package com.klybik.management.dto.survey;

import com.klybik.management.constant.enums.SurveyStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSurveyStatusRequest {
    private SurveyStatusEnum status;
}
