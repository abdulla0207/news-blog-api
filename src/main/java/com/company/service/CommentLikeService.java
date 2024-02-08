package com.company.service;

import com.company.entity.CommentLikeEntity;
import com.company.enums.LikeStatusEnum;
import com.company.exception.ItemNotFoundException;
import com.company.repository.CommentLikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final ResourceMessageService resourceMessageService;

    @Autowired
    public CommentLikeService(CommentLikeRepository commentLikeRepository, ResourceMessageService resourceMessageService){
        this.commentLikeRepository = commentLikeRepository;
        this.resourceMessageService = resourceMessageService;
    }

    public String likeComment(String commentId, Integer userId, String lang) {
        Optional<CommentLikeEntity> byCommentIdAndUserId = commentLikeRepository.findCommentByCommentIdAndUserId(commentId, userId);

        if(byCommentIdAndUserId.isEmpty()){
            CommentLikeEntity commentLikeEntity = new CommentLikeEntity();
            commentLikeEntity.setCommentId(commentId);
            commentLikeEntity.setLikeStatusEnum(LikeStatusEnum.LIKE);
            commentLikeEntity.setUserId(userId);
            commentLikeEntity.setCreatedAt(LocalDateTime.now());

            commentLikeRepository.save(commentLikeEntity);
            log.info("Comment liked {}", commentId);
            return resourceMessageService.getMessage("comment.like", lang);
        }

        CommentLikeEntity entity = byCommentIdAndUserId.get();

        if(entity.getLikeStatusEnum().equals(LikeStatusEnum.DISLIKE)){
            entity.setLikeStatusEnum(LikeStatusEnum.LIKE);
        }
        commentLikeRepository.save(entity);
        log.info("Comment liked {}", commentId);
        return resourceMessageService.getMessage("comment.like", lang);
    }

    public String dislikeComment(String commentId, Integer idFromHeader, String lang) {
        Optional<CommentLikeEntity> byCommentIdAndUserId = commentLikeRepository.findCommentByCommentIdAndUserId(commentId, idFromHeader);

        if(byCommentIdAndUserId.isEmpty()){
            CommentLikeEntity commentLikeEntity = new CommentLikeEntity();
            commentLikeEntity.setCommentId(commentId);
            commentLikeEntity.setLikeStatusEnum(LikeStatusEnum.DISLIKE);
            commentLikeEntity.setUserId(idFromHeader);
            commentLikeEntity.setCreatedAt(LocalDateTime.now());

            commentLikeRepository.save(commentLikeEntity);
            log.info("Comment disliked {}", commentId);
            return resourceMessageService.getMessage("comment.dislike", lang);
        }

        CommentLikeEntity entity = byCommentIdAndUserId.get();

        if(entity.getLikeStatusEnum().equals(LikeStatusEnum.LIKE)){
            entity.setLikeStatusEnum(LikeStatusEnum.DISLIKE);
        }
        commentLikeRepository.save(entity);
        log.info("Comment disliked {}", commentId);
        return resourceMessageService.getMessage("comment.dislike", lang);
    }

    public String removeLikeDislikeFromComment(Integer idFromHeader, String commentId, String lang) {
        Optional<CommentLikeEntity> commentByCommentIdAndUserId = commentLikeRepository.findCommentByCommentIdAndUserId(commentId, idFromHeader);
        if(commentByCommentIdAndUserId.isEmpty()) {
            log.warn("User has not liked or disliked the comment yet");
            throw new ItemNotFoundException(resourceMessageService.getMessage("comment.no.action", lang));
        }

        commentLikeRepository.deleteCommentLikeEntityByCommentIdAndUserId(commentId, idFromHeader);
        CommentLikeEntity commentLikeEntity = commentByCommentIdAndUserId.get();

        if(commentLikeEntity.getLikeStatusEnum().equals(LikeStatusEnum.LIKE)) {
            log.info("Comment like removed");
            return resourceMessageService.getMessage("comment.like.remove", lang);
        }
        log.info("Comment dislike removed");
        return resourceMessageService.getMessage("comment.dislike.remove", lang);
    }
}
