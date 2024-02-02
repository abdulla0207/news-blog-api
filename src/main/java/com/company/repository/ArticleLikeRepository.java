package com.company.repository;

import com.company.entity.ArticleLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLikeEntity, String> {

    @Query("select a from ArticleLikeEntity as a where a.articleUuid=?1 and a.userId = ?2")
    Optional<ArticleLikeEntity> findByArticleIdAndUserId(String articleId, Integer idFromHeader);

    void deleteArticleLikeEntityByArticleUuidAndUserId(String articleId, Integer userId);
}
