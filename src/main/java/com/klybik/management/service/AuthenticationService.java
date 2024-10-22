package com.klybik.management.service;

import com.klybik.management.config.JwtService;
import com.klybik.management.dto.auth.AuthenticationRequest;
import com.klybik.management.dto.auth.AuthenticationResponse;
import com.klybik.management.dto.auth.UpdatePassword;
import com.klybik.management.entity.User;
import com.klybik.management.exception.InvalidPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public void updatePassword(UpdatePassword request) {
        User user = userService.getUserByEmail(request.getEmail());

        if (!user.getPassword().equals(passwordEncoder.encode(request.getOldPassword()))) {
            throw new InvalidPasswordException();
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()))
                .setIsFirstStart(false);

        userService.saveUser(user);
    }

    public AuthenticationResponse authentication(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userService.getUserByEmail(request.getEmail());

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
