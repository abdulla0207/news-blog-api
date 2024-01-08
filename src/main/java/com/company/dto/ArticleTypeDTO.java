package com.company.dto;

import java.time.LocalDateTime;

public record ArticleTypeDTO(int id, String key, String nameUz,
                             String nameEn, boolean visible,
                             LocalDateTime createdAt, LocalDateTime updatedAt) {
}
