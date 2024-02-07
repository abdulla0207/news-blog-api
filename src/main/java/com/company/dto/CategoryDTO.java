package com.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryDTO(
        int id,
        @NotBlank(message = "Name UZ of the category must be filled")
        String nameUz,
        @NotBlank(message = "Name EN of the category must be filled")
        String nameEn,
        boolean visible,
        LocalDateTime createdAt,
        @NotBlank(message = "Key of the category must be filled")
        String key,
        @NotBlank(message = "Slag of the category must be filled")
        String slag
){

}
