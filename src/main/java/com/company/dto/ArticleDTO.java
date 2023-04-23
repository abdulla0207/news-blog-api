package com.company.dto;

import com.company.entity.CategoryEntity;
import com.company.entity.CommentEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ArticleStatusEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {
    private String uuid;
    private String titleUz;
    private String titleEn;
    private String descriptionUz;
    private String descriptionEn;
    private String contentUz;
    private String contentEn;
    private ArticleStatusEnum articleStatus;
    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;
    private boolean visible;
    private int categoryId;
    private CategoryDTO categoryDTO;
}
