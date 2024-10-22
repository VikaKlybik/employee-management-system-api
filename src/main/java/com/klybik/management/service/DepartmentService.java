package com.klybik.management.service;

import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.NotFound;
import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.Duplicate;
import com.klybik.management.entity.Department;
import com.klybik.management.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public Department getDepartmentById(UUID id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        NotFound.DEPARTMENT
                ));
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
