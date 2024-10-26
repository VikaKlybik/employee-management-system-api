package com.klybik.management.entity;

import com.klybik.management.constant.enums.EvaluationMethodEnum;
import com.klybik.management.constant.enums.SurveyStatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private EvaluationMethodEnum evaluationMethod;
    @Enumerated(EnumType.ORDINAL)
    private SurveyStatusEnum status;
    @OneToMany(mappedBy = "survey", cascade = CascadeType.PERSIST)
    private List<Question> questions;
}
