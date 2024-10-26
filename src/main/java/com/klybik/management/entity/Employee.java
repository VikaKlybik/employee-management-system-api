package com.klybik.management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    private JobTitle jobTitle;
    @OneToMany(mappedBy = "employee")
    private List<KPI> kpiList;
    @OneToOne(mappedBy = "employee")
    private User user;
}
