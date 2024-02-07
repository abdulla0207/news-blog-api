package com.company.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record RegionDTO(int id,
                        @NotBlank(message = "Key of the region must be filled")
                        String key,
                        @NotBlank(message = "Name UZ of the region must be filled")
                        String nameUz,
                        @NotBlank(message = "Name EN of the region must be filled")
                        String nameEn,
                        boolean visible,
                        LocalDateTime createdAt,
                        LocalDateTime updatedAt) {
}
