package com.klybik.management.controller;

import com.klybik.management.dto.auth.AuthenticationRequest;
import com.klybik.management.dto.auth.AuthenticationResponse;
import com.klybik.management.dto.auth.UpdatePassword;
import com.klybik.management.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "APIs for user authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/update-password")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody UpdatePassword request
    ) {
        return ResponseEntity.ok(authenticationService.updatePassword(request));
    }

    @PostMapping("/enter")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AuthenticationResponse> authentication(
            @RequestBody @Valid AuthenticationRequest request,
            HttpServletResponse response
    ) {
        AuthenticationResponse authenticationResponse = authenticationService.authentication(request);
//        Cookie cookie = new Cookie("tokens", authenticationResponse.getToken());
//        cookie.setMaxAge(24*60*60);
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");
//        response.addCookie(cookie);
        return ResponseEntity.ok(authenticationResponse);
    }
}