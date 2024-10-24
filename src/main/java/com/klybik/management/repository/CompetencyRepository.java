package com.klybik.management.repository;

import com.klybik.management.entity.Competency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompetencyRepository extends JpaRepository<Competency, UUID> {
    boolean existsByNameIgnoreCase(String name);
}
