package com.klybik.management.dto.survey;

import com.klybik.management.constant.enums.SurveyStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateSurveyStatusRequest {
    private SurveyStatusEnum status;
}
