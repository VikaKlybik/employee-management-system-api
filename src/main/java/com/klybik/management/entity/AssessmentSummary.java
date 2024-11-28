package com.klybik.management.entity;

import com.klybik.management.constant.enums.EvaluatorTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private BigDecimal assessmentSummary;
    private Integer totalReviews;
    @Enumerated(EnumType.STRING)
    private EvaluatorTypeEnum evaluatorType;
    @ManyToOne
    private Employee employee;
    @ManyToOne
    private Competency competency;
    @ManyToOne
    private Survey survey;
}
