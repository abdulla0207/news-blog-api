package com.company.dto.article;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ArticleCreateDTO(
        @Size(min = 5, max = 50, message = "Title length should be from 5 to 100 characters")
        String title,
        @Size(min = 30, max = 150, message = "Description of article should be from 50 to 150 characters")
        String description,
        @Size(min = 150, message = "Content of the article should have more than 150 characters")
        String content,
        @Positive(message = "Category Id should be positive")
        int categoryId,
        @Positive(message = "Region Id should be positive")
        int regionId,
        @Positive(message = "Language Id should be positive")
        int languageId,
        @Positive(message = "Article Type Id should be positive")
        int articleTypeId) {
}
