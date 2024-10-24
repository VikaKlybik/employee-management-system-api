package com.klybik.management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    private UUID id;
    private String name;
    @ManyToOne
    private Survey survey;
    @ManyToOne
    private Competency competency;
}
