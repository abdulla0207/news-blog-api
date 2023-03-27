package com.company.dto.auth;

import com.company.enums.ProfileRoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDTO {
    private String name;
    private String surname;
    private ProfileRoleEnum role;
    private String token;
}
