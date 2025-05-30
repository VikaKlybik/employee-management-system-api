package com.klybik.management.controller;

import com.klybik.management.dto.employee.EmployeeResponse;
import com.klybik.management.dto.employee.EmployeeUpdateRequest;
import com.klybik.management.dto.user.CreateUserRequest;
import com.klybik.management.dto.user.UserResponse;
import com.klybik.management.entity.Employee;
import com.klybik.management.entity.User;
import com.klybik.management.mapper.EmployeeMapper;
import com.klybik.management.mapper.UserMapper;
import com.klybik.management.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "APIs for working with user data")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final EmployeeMapper employeeMapper;

    @GetMapping
    public UserResponse getUserByEmail(@PathParam("email") String email) {
        User user = userService.getUserByEmail(email);
        return userMapper.toUserResponse(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        User user = userService.createUser(createUserRequest);
        return userMapper.toUserResponse(user);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(
            path = "/upload/{id}",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadProfileImage(@PathVariable UUID id, @RequestParam("file") MultipartFile file) throws IOException {
        userService.uploadImageToUser(id, file);
    }

    @PutMapping("/{userId}")
    public EmployeeResponse updateEmployee(@PathVariable UUID userId, @RequestBody EmployeeUpdateRequest employeeUpdateRequest) {
        Employee employee = userService.updateEmployee(userId, employeeUpdateRequest);
        return employeeMapper.toEmployeeResponse(employee);
    }
}
