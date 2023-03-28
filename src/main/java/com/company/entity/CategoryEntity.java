package com.company.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "category")
@Getter
@Setter
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name_ru")
    private String nameRu;
    @Column(name = "name_eng")
    private String nameEng;
    @Column
    private boolean visible;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
