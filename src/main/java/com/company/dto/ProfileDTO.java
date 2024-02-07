package com.company.dto;

import com.company.enums.ProfileRoleEnum;
import com.company.enums.ProfileStatusEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProfileDTO(
        int id,
        @NotBlank(message = "Name of the user must be filled")
        String name,
        @NotBlank(message = "Surname of the user must be filled")
        String surname,
        @Email(message = "Wrong email format")
        String email,
        @Pattern(regexp = "\\+998\\d{9}", message = "Phone number should be in the following format: +998 xx xxx-xx-xx")
        String phoneNumber,
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Password must be at least 8 characters with one uppercase, one lowercase, one digit, and one special character.")
        String password,
        ProfileStatusEnum statusEnum,
        ProfileRoleEnum roleEnum,
        LocalDateTime createdAt
){

}
