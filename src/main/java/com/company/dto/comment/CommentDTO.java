package com.company.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CommentDTO(
        String uuid,
        @NotBlank(message = "Content of the comment should be filled")
        String content,
        String articleId,
        Integer userId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,

        boolean visible) {
}
