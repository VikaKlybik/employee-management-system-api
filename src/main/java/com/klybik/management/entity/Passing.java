package com.klybik.management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "passing")
public class Passing {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Boolean isPass;
    @ManyToOne
    @JoinColumn(name = "evaluated_person_id")
    private Employee evaluatedPerson;
    @ManyToOne
    @JoinColumn(name = "evaluator_id")
    private Employee evaluator;
    @ManyToOne
    private Survey survey;
}
