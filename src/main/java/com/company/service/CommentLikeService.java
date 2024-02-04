package com.company.service;

import com.company.entity.CommentLikeEntity;
import com.company.enums.LikeStatusEnum;
import com.company.exception.ItemNotFoundException;
import com.company.repository.CommentLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;

    @Autowired
    public CommentLikeService(CommentLikeRepository commentLikeRepository){
        this.commentLikeRepository = commentLikeRepository;
    }

    public String likeComment(String commentId, Integer userId) {
        Optional<CommentLikeEntity> byCommentIdAndUserId = commentLikeRepository.findCommentByCommentIdAndUserId(commentId, userId);

        if(byCommentIdAndUserId.isEmpty()){
            CommentLikeEntity commentLikeEntity = new CommentLikeEntity();
            commentLikeEntity.setCommentId(commentId);
            commentLikeEntity.setLikeStatusEnum(LikeStatusEnum.LIKE);
            commentLikeEntity.setUserId(userId);
            commentLikeEntity.setCreatedAt(LocalDateTime.now());

            commentLikeRepository.save(commentLikeEntity);
            return "Comment Liked";
        }

        CommentLikeEntity entity = byCommentIdAndUserId.get();

        if(entity.getLikeStatusEnum().equals(LikeStatusEnum.DISLIKE)){
            entity.setLikeStatusEnum(LikeStatusEnum.LIKE);
        }
        return "Comment Liked";
    }

    public String dislikeComment(String commentId, Integer idFromHeader) {
        Optional<CommentLikeEntity> byCommentIdAndUserId = commentLikeRepository.findCommentByCommentIdAndUserId(commentId, idFromHeader);

        if(byCommentIdAndUserId.isEmpty()){
            CommentLikeEntity commentLikeEntity = new CommentLikeEntity();
            commentLikeEntity.setCommentId(commentId);
            commentLikeEntity.setLikeStatusEnum(LikeStatusEnum.DISLIKE);
            commentLikeEntity.setUserId(idFromHeader);
            commentLikeEntity.setCreatedAt(LocalDateTime.now());

            commentLikeRepository.save(commentLikeEntity);
            return "Comment Disliked";
        }

        CommentLikeEntity entity = byCommentIdAndUserId.get();

        if(entity.getLikeStatusEnum().equals(LikeStatusEnum.LIKE)){
            entity.setLikeStatusEnum(LikeStatusEnum.DISLIKE);
        }
        return "Comment Disliked";
    }

    public String removeLikeDislikeFromComment(Integer idFromHeader, String commentId) {
        Optional<CommentLikeEntity> commentByCommentIdAndUserId = commentLikeRepository.findCommentByCommentIdAndUserId(commentId, idFromHeader);
        if(commentByCommentIdAndUserId.isEmpty())
            throw new ItemNotFoundException("User has not liked or disliked the comment yet");

        commentLikeRepository.deleteCommentLikeEntityByCommentIdAndUserId(commentId, idFromHeader);
        CommentLikeEntity commentLikeEntity = commentByCommentIdAndUserId.get();

        if(commentLikeEntity.getLikeStatusEnum().equals(LikeStatusEnum.LIKE))
            return "Comment Like Removed";
        return "Comment Dislike Removed";
    }
}
