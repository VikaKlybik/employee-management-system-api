package com.klybik.management.dto.evaluators;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListOfPassingResponse {
    private List<PassingResponse> passing;
}
