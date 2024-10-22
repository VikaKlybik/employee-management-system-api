package com.klybik.management.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NotBlank(message = "Email is mandatory!")
    private String email;
    @NotBlank(message = "Password is mandatory!")
    private String password;
}
