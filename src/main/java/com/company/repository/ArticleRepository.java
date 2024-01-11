package com.company.repository;

import com.company.entity.ArticleEntity;
import org.springframework.data.domain.Page;
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

    @Query("select a from ArticleEntity as a where a.publish=false")
    Page<ArticleEntity> getArticlesForReview(Pageable pageable);

    @Query("select a from ArticleEntity as a order by a.titleUz")
    Page<ArticleEntity> findArticlesByTitle(Pageable pageable);



    List<ArticleEntity> findArticleEntitiesByTitleUzLikeIgnoreCase(String titleUz);

    Page<ArticleEntity> findArticleEntitiesByCategory_Key(Pageable pageable, String key);
}
