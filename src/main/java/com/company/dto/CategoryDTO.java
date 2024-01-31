package com.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryDTO(
        int id,
        String nameUz,
        String nameEn,
        boolean visible,
        LocalDateTime createdAt,
        String key,
        String slag
){

}
