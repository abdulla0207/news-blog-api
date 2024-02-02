package com.company.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateDTO(
        @NotBlank
        String nameUz,
        @NotBlank
        String nameEn,
        boolean visible,
        @NotBlank
        String key,
        @NotBlank
        String slag) {
}
