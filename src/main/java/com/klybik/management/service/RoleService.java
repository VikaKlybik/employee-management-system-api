package com.klybik.management.service;

import com.klybik.management.entity.Role;
import com.klybik.management.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.NotFound;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(
                        () -> new EntityNotFoundException(NotFound.ROLE.formatted(name))
                );
    }
}
