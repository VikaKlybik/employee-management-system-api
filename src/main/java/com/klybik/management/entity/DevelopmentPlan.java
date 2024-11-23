package com.klybik.management.entity;

import com.klybik.management.constant.enums.DevelopmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DevelopmentPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String comments;
    private String goal;
    @Enumerated(EnumType.ORDINAL)
    private DevelopmentStatus status;
    @ManyToOne
    private Employee employee;
    @ManyToOne
    private Survey survey;
    @ManyToOne
    private Competency competency;
}
