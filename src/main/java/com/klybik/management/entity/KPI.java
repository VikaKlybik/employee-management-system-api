package com.klybik.management.entity;

import com.klybik.management.constant.enums.MeasureUnitEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kpi")
public class KPI {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String description;
    private BigDecimal targetValue;
    @Enumerated(EnumType.ORDINAL)
    private MeasureUnitEnum measureUnit;
    private BigDecimal weight;
    @ManyToOne
    private Employee employee;
    @OneToMany(mappedBy = "kpi")
    private List<KPIAssessment> kpiAssessments;
    @ManyToOne
    private KPIPeriod kpiPeriod;
}
