package com.klybik.management.entity;

import com.klybik.management.constant.enums.EvaluatorTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Integer mark;
    @Enumerated(EnumType.STRING)
    private EvaluatorTypeEnum evaluatorType;
    @ManyToOne
    private Question question;
    @ManyToOne
    private Employee evaluatedEmployee;
}
