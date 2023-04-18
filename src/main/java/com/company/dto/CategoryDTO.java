package com.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDTO {

    private int id;
    private String nameRu;
    private String nameEng;
    private boolean visible;
    private LocalDateTime createdAt;
    private String key;

}
