package com.company.dto;

import com.company.enums.ProfileRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtDTO {
    private int id;
    private ProfileRoleEnum role;
}
