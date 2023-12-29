package com.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

//@Getter
//@Setter
//@JsonInclude(JsonInclude.Include.NON_NULL)
//public class CategoryDTO {
//
//    private int id;
//    private String nameUz;
//    private String nameEn;
//    private boolean visible;
//    private LocalDateTime createdAt;
//    private String key;
//    private String slag;
//
//}

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryDTO(
        int id,
        String nameUz,
        String nameEn,
        boolean visible,
        LocalDateTime createdAt,
        String key,
        String slag
){

}
