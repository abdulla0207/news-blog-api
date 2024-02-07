package com.company.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateDTO(
        @NotBlank(message = "Name UZ of the category must be filled")
        String nameUz,
        @NotBlank(message = "Name En of the category must be filled")
        String nameEn,
        boolean visible,
        @NotBlank(message = "Key of the category must be filled")
        String key,
        @NotBlank(message = "Slag of the category must be filled")
        String slag) {
}
