package com.klybik.management.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JobTitle {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private Boolean isLead;
    @ManyToOne
    private JobTitle lead;
    @ManyToOne
    private Department department;
    @OneToMany(mappedBy = "lead")
    private List<JobTitle> subordinates;
}
