package com.company.dto.authentication;

import com.company.enums.ProfileRoleEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponseDTO {
    private String name;
    private String surname;
    private ProfileRoleEnum role;
    private String token;
    private boolean resendVerification;
}
