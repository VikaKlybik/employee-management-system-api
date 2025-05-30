package com.klybik.management.repository;

import com.klybik.management.entity.JobTitle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobTitleRepository extends JpaRepository<JobTitle, UUID> {
    boolean existsByNameIgnoreCaseAndDepartmentId(String name, UUID departmentId);

    List<JobTitle> findAllByDepartmentId(UUID departmentId);
    Optional<JobTitle> findByDepartmentIdAndIsLeadIsTrueAndLeadIsNull(UUID departmentId);
}
