package com.company.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;

public record RegistrationDTO(
        @NotBlank(message = "Name of the user should be filled")
        String name,
        @NotBlank(message = "Surname of the user must be filled")
        String surname,
        @Email(message = "The format of email is wrong")
        String email,
        @Pattern(regexp = "\\+998\\d{9}", message = "Invalid Uzbek phone number")
        String phoneNumber,
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Password must be at least 8 characters with one uppercase, one lowercase, one digit, and one special character.")
        String password
){}
