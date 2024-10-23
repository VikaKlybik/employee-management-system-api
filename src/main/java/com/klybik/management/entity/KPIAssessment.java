package com.klybik.management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kpi_assessment")
public class KPIAssessment {
    @Id
    private UUID id;
    private BigDecimal actualValue;
    private LocalDateTime assessmentDate;
    private String comments;
    @ManyToOne
    private KPI kpi;
}
