package com.klybik.management.dto.user;

import com.klybik.management.constant.enums.UserRoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    @NotBlank(message = "First name is mandatory!")
    private String firstName;
    @NotBlank(message = "Last name is mandatory!")
    private String lastName;
    @NotBlank(message = "Email is mandatory!")
    @Email
    private String email;
    @NotBlank(message = "Password is mandatory!")
    private String password;
    private String role = UserRoleEnum.EMPLOYEE.getValue();
    private UUID jobTitleId;
}
