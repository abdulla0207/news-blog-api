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
    @Column
    private String title;
    @Column
    private String description;
    @Column(columnDefinition = "text")
    private String content;
//    @JoinColumn(name = "publisher_id", insertable = false, updatable = false)
//    @ManyToOne(fetch = FetchType.LAZY)
//    private ProfileEntity profile;
//    @JoinColumn(name = "category_id", insertable = false, updatable = false)
//    @OneToOne(fetch = FetchType.LAZY)
//    private CategoryEntity category;
//
//    @OneToMany(mappedBy = "article")
//    @Column(name = "comment")
//    private List<CommentEntity> commentEntity;
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
