package com.company.entity;

import com.company.enums.ArticleStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "article")
@Getter
@Setter
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Column(name = "title_uz")
    private String titleUz;
    @Column(name = "title_en")
    private String titleEn;

    @Column(name = "description_uz")
    private String descriptionUz;
    @Column(name = "description_en")
    private String descriptionEn;

    @Column(name = "content_uz", columnDefinition = "text")
    private String contentUz;
    @Column(name = "content_en", columnDefinition = "text")
    private String contentEn;

    @Column(name = "publisher_id")
    private Integer publisherId;
    @JoinColumn(name = "publisher_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ProfileEntity publisher;

    @Column(name = "category_id")
    private Integer categoryId;
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity category;

    @Column(name = "article_type_id")
    private Integer articleTypeId;
    @JoinColumn(name = "article_type_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ArticleTypeEntity articleType;

    @Column(name = "region_id")
    private Integer regionId;
    @JoinColumn(name = "region_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private RegionEntity region;

    @Column(name = "article_status")
    @Enumerated(EnumType.STRING)
    private ArticleStatusEnum articleStatus;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "published_at")
    private LocalDateTime publishedAt;
    @Column
    private boolean visible;
}
