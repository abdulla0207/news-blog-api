package com.company.dto;

import jakarta.validation.constraints.NotBlank;

public record ArticleTypeCreateDTO(
        @NotBlank(message = "Key of the Article Type must be filled")
        String key,
        @NotBlank(message = "Name UZ of the Article Type must be filled")
        String nameUz,
        @NotBlank(message = "Name EN of the Article Type must be filled")
        String nameEn) {
}
