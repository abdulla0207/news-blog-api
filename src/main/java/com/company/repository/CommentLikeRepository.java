package com.company.repository;

import com.company.entity.CommentLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLikeEntity, String> {

    Optional<CommentLikeEntity> findCommentByCommentIdAndUserId(String commentId, Integer userId);

    void deleteCommentLikeEntityByCommentIdAndUserId(String commentId, Integer idFromHeader);
}
