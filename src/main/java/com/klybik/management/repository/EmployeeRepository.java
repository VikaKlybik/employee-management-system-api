package com.klybik.management.repository;

import com.klybik.management.entity.Employee;
import com.klybik.management.entity.JobTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID>, JpaSpecificationExecutor<Employee> {
    Optional<Employee> findByUserId(UUID userId);
    Optional<Employee> findByJobTitleId(UUID jobTitleId);
    List<Employee> findAllByJobTitleIdAndUserIdNot(UUID jobTitle_id, UUID userId);
    List<Employee> findAllByJobTitleLeadId(UUID jobTitleLeadId);
    List<Employee> findAllByUserIdIn(List<UUID> evaluatorIds);
}
