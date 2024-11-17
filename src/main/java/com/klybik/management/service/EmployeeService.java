package com.klybik.management.service;

import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.NotFound;
import com.klybik.management.dto.filter.EmployeeFilterParam;
import com.klybik.management.dto.filter.GenerateDefaultEvaluatorsParam;
import com.klybik.management.entity.Employee;
import com.klybik.management.entity.User;
import com.klybik.management.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployee(EmployeeFilterParam employeeFilterParam) {
        Specification<Employee> specification = Specification
                .where(hasJobTitle(employeeFilterParam.getJobTitleId()))
                .and(hasDepartment(employeeFilterParam.getDepartmentId()))
                .and(hasLead(employeeFilterParam.getLeadId()))
                .and(hasName(employeeFilterParam.getSearch()));

        return employeeRepository.findAll(specification);
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

    public Specification<Employee> hasName(String searchName) {
        return (root, query, criteriaBuilder) -> {
            if (searchName == null || searchName.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            Join<Employee, User> userJoin = root.join("user");
            String[] searchTerms = searchName.trim().toLowerCase().split("\\s+");
            return Arrays.stream(searchTerms)
                    .map(word -> {
                        String searchPattern = "%" + word + "%";
                        return criteriaBuilder.or(
                                criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("firstName")), searchPattern),
                                criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("lastName")), searchPattern)
                        );
                    })
                    .reduce(criteriaBuilder::and)
                    .orElse(criteriaBuilder.conjunction());
        };
    }

    public Employee getByUserId(UUID userId) {
        return employeeRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(NotFound.EMPLOYEE.formatted(userId)));
    }

    public Employee getByEmployeeId(UUID employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(NotFound.EMPLOYEE.formatted(employeeId)));
    }

    public List<Employee> generateDefaultEvaluators(GenerateDefaultEvaluatorsParam generateDefaultEvaluatorsParam) {
        Employee employee = getByUserId(generateDefaultEvaluatorsParam.getUserId());
        List<Employee> evaluators = new ArrayList<>();

        switch (generateDefaultEvaluatorsParam.getMethod()) {
            case METHOD_270: {
                evaluators.addAll(getEvaluatorsUsingMethod270(employee));
                break;
            }
            case METHOD_360: {
                evaluators.addAll(getEvaluatorsUsingMethod360(employee));
                break;
            }
        };

        return evaluators;
    };

    private List<Employee> getEvaluatorsUsingMethod270(Employee evaluatedEmployee) {
        List<Employee> evaluators = new ArrayList<>(getColleagues(evaluatedEmployee));
        if(evaluatedEmployee.getJobTitle().getLead()!=null) {
            evaluators.addAll(getLeads(evaluatedEmployee));
        }
        evaluators.add(evaluatedEmployee);
        return evaluators;
    }

    private List<Employee> getEvaluatorsUsingMethod360(Employee evaluatedEmployee) {
        List<Employee> evaluators = getEvaluatorsUsingMethod270(evaluatedEmployee);
        evaluators.addAll(getSubordinates(evaluatedEmployee));
        return evaluators;
    }

    private List<Employee> getColleagues(Employee employee) {
        return employeeRepository.findAllByJobTitleIdAndUserIdNot(
                employee.getJobTitle().getId(),
                employee.getUser().getId()
        );
    }

    private List<Employee> getLeads(Employee employee) {
        return employeeRepository.findAllByJobTitleIdAndUserIdNot(
                employee.getJobTitle().getLead().getId(),
                employee.getUser().getId()
        );
    }

    private List<Employee> getSubordinates(Employee employee) {
        return employeeRepository.findAllByJobTitleLeadId(employee.getJobTitle().getId());
    }

    public List<Employee> getAllEmployeeInListOfId(List<UUID> evaluatorIds) {
        return employeeRepository.findAllByUserIdIn(evaluatorIds);
    }
}
