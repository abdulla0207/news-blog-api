package com.company.dto.comment;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record CommentReplyDTO(
        String uuid,
        String content,
        String articleId,
        Integer userId,
        LocalDateTime createdAt,
        String parentCommentId
) {
}
