package com.klybik.management.repository;

import com.klybik.management.entity.KPIPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface KPIPeriodRepository extends JpaRepository<KPIPeriod, UUID> {
}
