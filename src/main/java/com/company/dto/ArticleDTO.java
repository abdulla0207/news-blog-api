package com.company.dto;

import com.company.enums.ArticleStatusEnum;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ArticleDTO(
        String uuid,
        String titleUz,
        String titleEn,
        String descriptionUz,
        String descriptionEn,
        String contentUz,
        String contentEn,
        ArticleStatusEnum articleStatus,
        LocalDateTime createdAt,
        LocalDateTime publishedAt,
        boolean visible,
        int categoryId,
        int regionId,
        int articleTypeId,
        boolean publish
) {}