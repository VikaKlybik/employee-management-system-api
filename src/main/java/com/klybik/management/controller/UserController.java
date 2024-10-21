package com.klybik.management.controller;

import com.klybik.management.dto.user.CreateUserRequest;
import com.klybik.management.dto.user.UserResponse;
import com.klybik.management.entity.User;
import com.klybik.management.mapper.UserMapper;
import com.klybik.management.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "APIs for working with user data")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping()
    public UserResponse getUserByEmail(@PathParam("email") String email) {
        User user = userService.getUserByEmail(email);
        return userMapper.toUserResponse(user);
    }

    @PostMapping
    public UserResponse createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        User user = userService.createUser(createUserRequest);
        return userMapper.toUserResponse(user);
    }
}
