package com.company.dto;

import jakarta.validation.constraints.NotBlank;

public record RegionByLanguageDTO(int id,
                                  @NotBlank(message = "Key of the region must be filled")
                                  String key,
                                  @NotBlank(message = "Name of the region must be filled")
                                  String name) {
}
