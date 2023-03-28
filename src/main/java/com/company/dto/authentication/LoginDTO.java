package com.company.dto.authentication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    private String email;
    private String phoneNumber;
    private String password;
}
