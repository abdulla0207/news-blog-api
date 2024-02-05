package com.company.service;

import com.company.dto.article.ArticleLikeDTO;
import com.company.dto.article.ArticleShortDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.ArticleLikeEntity;
import com.company.enums.LikeStatusEnum;
import com.company.exception.ItemNotFoundException;
import com.company.repository.ArticleLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleLikeService {
    private final ArticleLikeRepository articleLikeRepository;

    private final ArticleService articleService;

    @Autowired
    public ArticleLikeService(ArticleLikeRepository articleLikeRepository, ArticleService articleService){
        this.articleLikeRepository = articleLikeRepository;
        this.articleService = articleService;
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

    public List<ArticleShortDTO> getLikedArticlesForUser(Integer idFromHeader) {
        List<ArticleEntity> articlesByUserAndStatus = articleService.findArticlesByUserAndStatus(idFromHeader, LikeStatusEnum.LIKE);

        List<ArticleShortDTO> response = new ArrayList<>();

        articlesByUserAndStatus.forEach(articleEntity -> {
            ArticleShortDTO shortDTO = new ArticleShortDTO(articleEntity.getUuid(), articleEntity.getTitle(),
                    articleEntity.getDescription(), articleEntity.getPublishedAt());

            response.add(shortDTO);
        });

        return response;
    }

    public int getLikesForArticles(String articleId, LikeStatusEnum likeStatusEnum) {
        int likeCount = articleLikeRepository.countLikeStatusForSpecificArticle(articleId, likeStatusEnum);


        return likeCount;
    }
}
