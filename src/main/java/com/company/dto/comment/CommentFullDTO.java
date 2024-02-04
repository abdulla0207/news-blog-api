package com.company.dto.comment;

import lombok.Getter;

import java.time.LocalDateTime;

public record CommentFullDTO(
        String uuid,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        ProfileForCommentDTO profile
) {
}
