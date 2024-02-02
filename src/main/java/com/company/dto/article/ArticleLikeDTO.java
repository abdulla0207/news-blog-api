package com.company.dto.article;

import com.company.enums.LikeStatusEnum;

public record ArticleLikeDTO(String uuid,
                             LikeStatusEnum likeStatusEnum) {
}
