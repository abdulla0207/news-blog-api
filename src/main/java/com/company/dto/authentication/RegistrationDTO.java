package com.company.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegistrationDTO(
        @NotBlank
        String name,
        @NotBlank
        String surname,
        @Email(message = "The format of email is wrong")
        String email,
        @Pattern(regexp = "\\+998\\d{9}", message = "Invalid Uzbek phone number")
        String phoneNumber,
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Password must be at least 8 characters with one uppercase, one lowercase, one digit, and one special character.")
        String password
){}
