package com.klybik.management.service;

import com.klybik.management.dto.filter.EmployeeFilterParam;
import com.klybik.management.entity.Employee;
import com.klybik.management.repository.EmployeeRepository;
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
                .and(hasDepartment(employeeFilterParam.getDepartmentId()));

        Pageable pageable = PageRequest.of(employeeFilterParam.getPageNumber(), employeeFilterParam.getPageSize());
        return employeeRepository.findAll(specification, pageable);
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
            if(departmentId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("jobTitle").get("department").get("id"), departmentId);
        });
    }

}
