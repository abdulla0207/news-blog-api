package com.company.entity;

import com.company.enums.LikeStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "article_like")
@Getter
@Setter
public class ArticleLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Column(name = "article_id")
    private String articleUuid;
    @JoinColumn(name = "article_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ArticleEntity articleEntity;

    @Column(name = "user_id")
    private Integer userId;
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ProfileEntity profileEntity;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "status")
    private LikeStatusEnum likeStatusEnum;
}
