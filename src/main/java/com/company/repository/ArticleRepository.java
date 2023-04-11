package com.company.repository;

import com.company.entity.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, String> {

    @Query("select a from ArticleEntity as a order by a.publishedAt")
    Page<ArticleEntity> findArticlesOrderByPublishedDate(Pageable pageable);

    @Query("select a from ArticleEntity as a order by a.title")
    Page<ArticleEntity> findArticlesOrderByTitle(Pageable pageable);


}
