package com.company.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "article_type")
@Getter
@Setter
public class ArticleTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "key")
    private String key;
    @Column(name = "name_uz")
    private String nameUz;
    @Column(name = "name_en")
    private String nameEn;
    @Column(name = "visible")
    private boolean visible;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
