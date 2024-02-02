package com.company.service;

import com.company.dto.article.ArticleLikeDTO;
import com.company.entity.ArticleLikeEntity;
import com.company.enums.LikeStatusEnum;
import com.company.exception.ItemNotFoundException;
import com.company.repository.ArticleLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ArticleLikeService {
    private final ArticleLikeRepository articleLikeRepository;

    @Autowired
    public ArticleLikeService(ArticleLikeRepository articleLikeRepository){
        this.articleLikeRepository = articleLikeRepository;
    }
    public String likeArticle(String articleId, Integer idFromHeader) {
        Optional<ArticleLikeEntity> byArticleIdAndUserId = articleLikeRepository.findByArticleIdAndUserId(articleId, idFromHeader);
        if(byArticleIdAndUserId.isEmpty()){
            ArticleLikeEntity entity = new ArticleLikeEntity();

            entity.setArticleUuid(articleId);
            entity.setUserId(idFromHeader);
            entity.setLikeStatusEnum(LikeStatusEnum.LIKE);
            entity.setCreatedAt(LocalDateTime.now());
            articleLikeRepository.save(entity);
            return "Article liked";
        }

        ArticleLikeEntity entity = byArticleIdAndUserId.get();
        if(entity.getLikeStatusEnum().equals(LikeStatusEnum.DISLIKE)){
            entity.setLikeStatusEnum(LikeStatusEnum.LIKE);
        }
        return "Article liked";
    }

    public String dislikeArticle(String articleId, Integer idFromHeader) {
        Optional<ArticleLikeEntity> byArticleIdAndUserId = articleLikeRepository.findByArticleIdAndUserId(articleId, idFromHeader);
        if(byArticleIdAndUserId.isEmpty()){
            ArticleLikeEntity entity = new ArticleLikeEntity();

            entity.setArticleUuid(articleId);
            entity.setUserId(idFromHeader);
            entity.setLikeStatusEnum(LikeStatusEnum.DISLIKE);
            entity.setCreatedAt(LocalDateTime.now());
            articleLikeRepository.save(entity);
            return "Article disliked";
        }

        ArticleLikeEntity entity = byArticleIdAndUserId.get();
        if(entity.getLikeStatusEnum().equals(LikeStatusEnum.LIKE)){
            entity.setLikeStatusEnum(LikeStatusEnum.DISLIKE);
        }
        return "Article Disliked";
    }

    public String removeLikeDislikeFromArticle(Integer idFromHeader, String articleId) {
        Optional<ArticleLikeEntity> byArticleIdAndUserId = articleLikeRepository.findByArticleIdAndUserId(articleId, idFromHeader);
        if(byArticleIdAndUserId.isEmpty())
            throw new ItemNotFoundException("Article is not liked or disliked for this user");

        articleLikeRepository.deleteArticleLikeEntityByArticleUuidAndUserId(articleId, idFromHeader);
        ArticleLikeEntity entity = byArticleIdAndUserId.get();

        if(entity.getLikeStatusEnum().equals(LikeStatusEnum.LIKE))
            return "Article Like Removed";
        return "Article Dislike Removed";
    }

    public ArticleLikeDTO hasUserLikedOrDisliked(String articleId, Integer idFromHeader) {
        Optional<ArticleLikeEntity> byArticleIdAndUserId = articleLikeRepository.findByArticleIdAndUserId(articleId, idFromHeader);

        if(byArticleIdAndUserId.isEmpty())
            throw new ItemNotFoundException("No action yet");

        ArticleLikeEntity entity = byArticleIdAndUserId.get();

        ArticleLikeDTO response = new ArticleLikeDTO(entity.getUuid(), entity.getLikeStatusEnum());

        return response;
    }
}
