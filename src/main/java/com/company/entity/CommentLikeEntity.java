package com.company.entity;

import com.company.enums.LikeStatusEnum;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment_like")
public class CommentLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Column(name = "article_id")
    private String articleId;
    @JoinColumn(name = "article_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ArticleEntity articleEntity;

    @Column(name = "user_id")
    private Integer userId;
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ProfileEntity profile;

    @Column(name = "created_date")
    private LocalDateTime createdAt;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private LikeStatusEnum likeStatusEnum;
}
