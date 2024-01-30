package com.company.mapper;

import java.time.LocalDateTime;

public interface IArticleShortViewInfo {

     String getUuid();
     String getTitle();
     String getDescription();
     LocalDateTime getPublishedDate();
}
