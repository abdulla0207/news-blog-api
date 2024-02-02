package com.company.dto.article;

import java.time.LocalDateTime;

public record ArticleShortDTO(String uuid, String title, String description, LocalDateTime publishedDate) {
}
