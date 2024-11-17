package com.klybik.management.repository;

import com.klybik.management.entity.Passing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PassingRepository extends JpaRepository<Passing, UUID> {
}
