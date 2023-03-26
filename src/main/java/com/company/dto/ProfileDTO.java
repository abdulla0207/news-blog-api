package com.company.dto;

import com.company.enums.ProfileRoleEnum;
import com.company.enums.ProfileStatusEnum;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProfileDTO {
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private String password;
    private ProfileStatusEnum status;
    private ProfileRoleEnum role;
    private LocalDateTime createdAt;
}
