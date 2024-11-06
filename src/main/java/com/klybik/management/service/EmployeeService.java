package com.klybik.management.service;

import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.NotFound;
import com.klybik.management.dto.filter.EmployeeFilterParam;
import com.klybik.management.entity.Employee;
import com.klybik.management.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }


    public Page<Employee> getAllEmployee(EmployeeFilterParam employeeFilterParam) {
        Specification<Employee> specification = Specification
                .where(hasJobTitle(employeeFilterParam.getJobTitleId()))
                .and(hasDepartment(employeeFilterParam.getDepartmentId()))
                .and(hasLead(employeeFilterParam.getLeadId()));

        Pageable pageable = PageRequest.of(employeeFilterParam.getPageNumber(), employeeFilterParam.getPageSize());
        return employeeRepository.findAll(specification, pageable);
    }

    private Specification<Employee> hasLead(UUID leadId) {
        return ((root, query, criteriaBuilder) -> {
            if (leadId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("jobTitle").get("lead").get("id"), leadId);
        });
    }

    private Specification<Employee> hasJobTitle(UUID jobTitleId) {
        return (root, query, criteriaBuilder) -> {
            if (jobTitleId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("jobTitle").get("id"), jobTitleId);
        };
    }

    private Specification<Employee> hasDepartment(UUID departmentId) {
        return ((root, query, criteriaBuilder) -> {
            if (departmentId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("jobTitle").get("department").get("id"), departmentId);
        });
    }


    public Employee getByUserId(UUID userId) {
        return employeeRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(NotFound.EMPLOYEE));
    }

    public Employee getByEmployeeId(UUID employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(NotFound.EMPLOYEE));
    }

}
