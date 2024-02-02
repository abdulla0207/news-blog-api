package com.company.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    @Email(message = "The format of email is wrong")
    private String email;
    @Pattern(regexp = "\\+998\\d{9}", message = "Invalid Uzbek phone number")
    private String phoneNumber;
    @NotBlank
    private String password;
}
