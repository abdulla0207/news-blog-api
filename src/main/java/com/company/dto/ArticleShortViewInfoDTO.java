package com.company.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public record ArticleShortViewInfoDTO(String uuid, String title, String description,
                                      LocalDateTime publishedDate) {
}
