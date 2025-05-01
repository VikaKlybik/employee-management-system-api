package com.klybik.management.service;

import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.Logic;
import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.NotFound;
import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.Duplicate;
import com.klybik.management.entity.Department;
import com.klybik.management.entity.Employee;
import com.klybik.management.entity.JobTitle;
import com.klybik.management.exception.DeleteEntityException;
import com.klybik.management.repository.DepartmentRepository;
import com.klybik.management.repository.EmployeeRepository;
import com.klybik.management.repository.JobTitleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final JobTitleRepository jobTitleRepository;

    public Department getDepartmentById(UUID id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        NotFound.DEPARTMENT
                ));
    }

    public Employee getHeadOfDepartment(UUID departmentId) {
        Optional<JobTitle> headPosition = jobTitleRepository.findByDepartmentIdAndIsLeadIsTrueAndLeadIsNull(departmentId);
        return headPosition
                .flatMap(jobTitle ->
                        employeeRepository.findByJobTitleId(jobTitle.getId())
                ).orElse(null);
    }

    public Department saveDepartment(Department department) {
        validateDepartmentExistByName(department.getName());
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public void deleteById(UUID id) {
        if(departmentRepository.existsById(id)) {
            Department department = getDepartmentById(id);
            if(department.getJobs() != null && !department.getJobs().isEmpty()) {
                throw new DeleteEntityException(Logic.DEPARTMENT_DELETE_FAILED.formatted(department.getId()));
            }
            departmentRepository.deleteById(id);
        }
    }

    public Department updateDepartment(Department department) {
        getDepartmentById(department.getId());
        validateDepartmentExistByName(department.getName());
        return departmentRepository.save(department);
    }

    private void validateDepartmentExistByName(String name) {
        if(departmentRepository.existsByNameIgnoreCase(name)) {
            throw new DuplicateKeyException(Duplicate.DEPARTMENT.formatted(name));
        }
    }
}
