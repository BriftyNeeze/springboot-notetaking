package com.example.note_taking.dto;

import com.example.note_taking.models.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileRegisterationDto {
    @NotEmpty(message = "Username required")
    private String username;
    @NotEmpty(message = "First name required")
    private String firstName;
    @NotEmpty(message = "Last name required")
    private String lastName;
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Valid Email required")
    private String email;
    @NotEmpty(message = "Password Required")
    private String password;
    private UserRole userRole;
}
