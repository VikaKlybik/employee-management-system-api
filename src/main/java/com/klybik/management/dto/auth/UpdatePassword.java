package com.klybik.management.dto.auth;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePassword {
    private String email;
    private String newPassword;
}
