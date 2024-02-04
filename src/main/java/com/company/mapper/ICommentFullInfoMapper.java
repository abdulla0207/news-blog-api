package com.company.mapper;

import java.time.LocalDateTime;

public interface ICommentFullInfoMapper {
    String getUuid();
    String getContent();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    Integer getUserId();
    String getUserName();
    String getUserSurname();
}
