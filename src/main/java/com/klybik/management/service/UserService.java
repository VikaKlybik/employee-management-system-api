package com.klybik.management.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.Duplicate;
import com.klybik.management.constant.EmployeeManagementSystemConstant.Error.NotFound;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final JobTitleService jobTitleService;
    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;
    private final Cloudinary cloudinary;
    private static final List<String> DEFAULT_PROFILE_PHOTO_URL = List.of(
            "http://res.cloudinary.com/dglw9cz06/image/upload/c_fill,h_400,w_400/0e8a7f74-1e3a-4eb3-9915-00169a09158d.jpg",
            "http://res.cloudinary.com/dglw9cz06/image/upload/c_fill,h_400,w_400/b9c2103d-e517-4e3d-add5-11f5f5c2c579.jpg",
            "http://res.cloudinary.com/dglw9cz06/image/upload/c_fill,h_400,w_400/673eb4d2-6c76-451a-9569-1654000cabd6.jpg"
    );

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
        Random random = new Random();
        String randomProfilePhotoUrl = DEFAULT_PROFILE_PHOTO_URL.get(random.nextInt(DEFAULT_PROFILE_PHOTO_URL.size()));
        User user = User.builder()
                .firstName(createUserRequest.getFirstName())
                .lastName(createUserRequest.getLastName())
                .email(createUserRequest.getEmail())
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .profilePhotoUrl(randomProfilePhotoUrl)
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

    public void uploadImageToUser(UUID userId, MultipartFile multipartFile) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(NotFound.USER.formatted(userId)));
        Map<String, Object> uploadParams = new HashMap<>();
        String imageTag = UUID.randomUUID().toString();
        uploadParams.put("public_id", imageTag);
        uploadParams.put("width", 400);
        uploadParams.put("height", 400);
        uploadParams.put("format", "jpg");
        uploadParams.put("crop", "fill");cloudinary.uploader()
                .upload(multipartFile.getBytes(), uploadParams);
        String profilePhotoUrl = cloudinary.url()
                .transformation(new Transformation<>()
                        .height(400)
                        .width(400)
                        .crop("fill"))
                .generate("%s.jpg".formatted(imageTag));

        user.setProfilePhotoUrl(profilePhotoUrl);
        userRepository.save(user);
    }
}
