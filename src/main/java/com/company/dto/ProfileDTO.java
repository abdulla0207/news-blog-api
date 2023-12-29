package com.company.dto;

import com.company.enums.ProfileRoleEnum;
import com.company.enums.ProfileStatusEnum;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProfileDTO(
        int id,
        String name,
        String surname,
        String email,
        String phoneNumber,
        String password,
        ProfileStatusEnum statusEnum,
        ProfileRoleEnum roleEnum,
        LocalDateTime createdAt
){

}
