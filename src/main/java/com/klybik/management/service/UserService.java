package com.klybik.management.service;

import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.NotFound;
import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.Duplicate;
import com.klybik.management.constant.enums.UserRoleEnum;
import com.klybik.management.dto.user.CreateUserRequest;
import com.klybik.management.entity.Employee;
import com.klybik.management.entity.User;
import com.klybik.management.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final JobTitleService jobTitleService;
    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        NotFound.USER.formatted(email))
                );
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public boolean existsUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public User createUser(CreateUserRequest createUserRequest) {
        if (existsUserByEmail(createUserRequest.getEmail())) {
            throw new DuplicateKeyException(
                    Duplicate.USER.formatted(createUserRequest.getEmail())
            );
        }

        User user = User.builder()
                .firstName(createUserRequest.getFirstName())
                .lastName(createUserRequest.getLastName())
                .email(createUserRequest.getEmail())
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .build();

        if (createUserRequest.getRole().equals(UserRoleEnum.EMPLOYEE.getValue())) {
            user.setRole(
                    roleService.getRoleByName(createUserRequest.getRole())
            );
            Employee employee = Employee.builder()
                    .jobTitle(jobTitleService.getJobTitleById(createUserRequest.getJobTittleId()))
                    .build();
            employeeService.saveEmployee(employee);
            user.setEmployee(employee);
            return saveUser(user);
        } else if (createUserRequest.getRole().equals(UserRoleEnum.ADMIN.getValue()) || createUserRequest.getRole().equals(UserRoleEnum.MANAGER.getValue())) {
            user.setRole(
                    roleService.getRoleByName(createUserRequest.getRole())
            );
            return saveUser(user);
        } else {
            throw new EntityNotFoundException(
                    NotFound.ROLE.formatted(createUserRequest.getRole())
            );
        }
    }
}
