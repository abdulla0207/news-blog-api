package com.company.dto.article;


import com.company.dto.CategoryByLanguageDTO;
import com.company.dto.RegionByLanguageDTO;

import java.time.LocalDateTime;

public record ArticleFullDTO(
        String uuid,
        String title,
        String description,
        String content,
        LocalDateTime published_date,
        RegionByLanguageDTO region,
        CategoryByLanguageDTO category
        ) {
}
