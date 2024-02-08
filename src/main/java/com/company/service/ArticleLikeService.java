package com.company.service;

import com.company.dto.article.ArticleLikeDTO;
import com.company.dto.article.ArticleShortDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.ArticleLikeEntity;
import com.company.enums.LikeStatusEnum;
import com.company.exception.ItemNotFoundException;
import com.company.repository.ArticleLikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ArticleLikeService {
    private final ArticleLikeRepository articleLikeRepository;
    private final ResourceMessageService resourceMessageService;

    @Autowired
    public ArticleLikeService(ArticleLikeRepository articleLikeRepository, ResourceMessageService resourceMessageService){
        this.articleLikeRepository = articleLikeRepository;
        this.resourceMessageService = resourceMessageService;
    }
    public String likeArticle(String articleId, Integer idFromHeader, String lang) {
        Optional<ArticleLikeEntity> byArticleIdAndUserId = articleLikeRepository.findByArticleIdAndUserId(articleId, idFromHeader);
        if(byArticleIdAndUserId.isEmpty()){
            ArticleLikeEntity entity = new ArticleLikeEntity();

            entity.setArticleUuid(articleId);
            entity.setUserId(idFromHeader);
            entity.setLikeStatusEnum(LikeStatusEnum.LIKE);
            entity.setCreatedAt(LocalDateTime.now());
            articleLikeRepository.save(entity);
            return resourceMessageService.getMessage("article.like", lang);
        }

        ArticleLikeEntity entity = byArticleIdAndUserId.get();
        if(entity.getLikeStatusEnum().equals(LikeStatusEnum.DISLIKE)){
            entity.setLikeStatusEnum(LikeStatusEnum.LIKE);
        }
        return resourceMessageService.getMessage("article.like", lang);
    }

    public String dislikeArticle(String articleId, Integer idFromHeader, String lang) {
        Optional<ArticleLikeEntity> byArticleIdAndUserId = articleLikeRepository.findByArticleIdAndUserId(articleId, idFromHeader);
        if(byArticleIdAndUserId.isEmpty()){
            ArticleLikeEntity entity = new ArticleLikeEntity();

            entity.setArticleUuid(articleId);
            entity.setUserId(idFromHeader);
            entity.setLikeStatusEnum(LikeStatusEnum.DISLIKE);
            entity.setCreatedAt(LocalDateTime.now());
            articleLikeRepository.save(entity);
            return resourceMessageService.getMessage("article.disliked", lang);
        }

        ArticleLikeEntity entity = byArticleIdAndUserId.get();
        if(entity.getLikeStatusEnum().equals(LikeStatusEnum.LIKE)){
            entity.setLikeStatusEnum(LikeStatusEnum.DISLIKE);
        }
        return resourceMessageService.getMessage("article.disliked", lang);
    }

    public String removeLikeDislikeFromArticle(Integer idFromHeader, String articleId, String lang) {
        Optional<ArticleLikeEntity> byArticleIdAndUserId = articleLikeRepository.findByArticleIdAndUserId(articleId, idFromHeader);
        if(byArticleIdAndUserId.isEmpty()) {
            log.warn("Article is not liked or disliked for the user");
            throw new ItemNotFoundException(resourceMessageService.getMessage("article.like.not.found", lang));
        }

        articleLikeRepository.deleteArticleLikeEntityByArticleUuidAndUserId(articleId, idFromHeader);
        ArticleLikeEntity entity = byArticleIdAndUserId.get();

        if(entity.getLikeStatusEnum().equals(LikeStatusEnum.LIKE))
            return "Article Like Removed";
        return resourceMessageService.getMessage("article.dislike.remove", lang);
    }

    public ArticleLikeDTO hasUserLikedOrDisliked(String articleId, Integer idFromHeader, String lang) {
        Optional<ArticleLikeEntity> byArticleIdAndUserId = articleLikeRepository.findByArticleIdAndUserId(articleId, idFromHeader);

        if(byArticleIdAndUserId.isEmpty()) {
            log.warn("Article not liked for the user yet {}", idFromHeader);
            throw new ItemNotFoundException(resourceMessageService.getMessage("article.like.not.found", lang));
        }

        ArticleLikeEntity entity = byArticleIdAndUserId.get();

        ArticleLikeDTO response = new ArticleLikeDTO(entity.getUuid(), entity.getLikeStatusEnum());

        return response;
    }

    public int getLikesForArticles(String articleId, LikeStatusEnum likeStatusEnum) {
        int likeCount = articleLikeRepository.countLikeStatusForSpecificArticle(articleId, likeStatusEnum);

        return likeCount;
    }
}
