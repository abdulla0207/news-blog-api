package com.company.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "category")
@Getter
@Setter
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name_uz")
    private String nameUz;
    @Column(name = "name_en")
    private String nameEn;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    // KEY field - is to get by category key, not id and name
    @Column
    private String key = String.valueOf(UUID.randomUUID());
    @Column
    private boolean isVisible = true;
    @Column
    private String slag;
}
