package com.company.dto;

import jakarta.validation.constraints.NotBlank;

public record ArticleTypeCreateDTO(
        @NotBlank
        String key,
        @NotBlank
        String nameUz,
        @NotBlank
        String nameEn) {
}
