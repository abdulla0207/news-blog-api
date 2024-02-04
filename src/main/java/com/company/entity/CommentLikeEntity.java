package com.company.entity;

import com.company.enums.LikeStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment_like")
@Getter
@Setter
public class CommentLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Column(name = "comment_id")
    private String commentId;
    @JoinColumn(name = "comment_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private CommentEntity commentEntity;

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
