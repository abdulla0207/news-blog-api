package com.company.dto.comment;

import lombok.Getter;

@Getter
public record ProfileForCommentDTO(Integer id,
                                   String name,
                                   String surname) {
}
