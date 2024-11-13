package com.klybik.management.repository;

import com.klybik.management.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID>, JpaSpecificationExecutor<Employee> {
    Optional<Employee> findByUserId(UUID userId);
    List<Employee> findAllByJobTitleIdAndUserIdNot(UUID jobTitle_id, UUID userId);
    List<Employee> findAllByJobTitleLeadId(UUID jobTitleLeadId);
}
