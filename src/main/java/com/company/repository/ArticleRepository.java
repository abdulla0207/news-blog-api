package com.company.repository;

import com.company.entity.ArticleEntity;
import com.company.enums.ArticleStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, String> {

    @Query("select a from ArticleEntity as a order by a.publishedAt")
    Page<ArticleEntity> findArticlesByPublishedDate(Pageable pageable);

    @Query("select a from ArticleEntity as a where a.articleStatus=com.company.enums.ArticleStatusEnum.NOT_PUBLISHED " +
            "and a.moderatorAction=com.company.enums.ModeratorActionEnum.APPROVE")
    Page<ArticleEntity> getArticlesForPublish(Pageable pageable);

    @Query("select a from ArticleEntity as a order by a.titleUz")
    Page<ArticleEntity> findArticlesByTitle(Pageable pageable);

    List<ArticleEntity> findArticleEntitiesByTitleUzLikeIgnoreCase(String titleUz);

    Page<ArticleEntity> findArticleEntitiesByCategory_Key(Pageable pageable, String key);

    @Query("select a from ArticleEntity as a where a.moderatorAction=com.company.enums.ModeratorActionEnum.NOT_REVIEWED")
    Page<ArticleEntity> getArticlesForReview(PageRequest of);

    @Query("select a from ArticleEntity as a where a.articleTypeId=?1 and a.articleStatus=com.company.enums.ArticleStatusEnum.PUBLISHED order by a.createdAt desc limit 5")
    List<ArticleEntity> findLastFiveByType(int typeId);

    List<ArticleEntity> getTop8ByArticleStatusAndUuidNotInOrderByCreatedAt(ArticleStatusEnum status, List<String> uuid);
}
