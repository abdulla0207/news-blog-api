package com.company.repository;

import com.company.entity.ArticleEntity;
import com.company.enums.ArticleStatusEnum;
import com.company.mapper.IArticleShortViewInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, String> {

    @Query("SELECT a FROM ArticleEntity a WHERE a.languageId = ?1 ORDER BY a.createdAt DESC")
    Page<ArticleEntity> findAllByLanguageCode(int languageId, Pageable pageable);

    @Query("select a from ArticleEntity as a where a.languageId = :langId order by a.publishedAt")
    Page<ArticleEntity> findArticlesByPublishedDate(int langId, Pageable pageable);

    @Query("select a from ArticleEntity as a where a.articleStatus=com.company.enums.ArticleStatusEnum.NOT_PUBLISHED " +
            "and a.moderatorAction=com.company.enums.ModeratorActionEnum.APPROVE")
    Page<ArticleEntity> getArticlesForPublish(Pageable pageable);

    @Query("select a from ArticleEntity as a where a.languageId = :langId and a.articleStatus = com.company.enums.ArticleStatusEnum.PUBLISHED order by a.title")
    Page<ArticleEntity> findArticlesByTitle(Pageable pageable, int langId);

    List<ArticleEntity> findArticleEntitiesByTitleLikeIgnoreCaseAndLanguageIdAndArticleStatus(String title, int languageId, ArticleStatusEnum articleStatusEnum);

    Page<ArticleEntity> findArticleEntitiesByCategory_KeyAndLanguageId(Pageable pageable, String key, int languageId);

    @Query("select a from ArticleEntity as a where a.moderatorAction=com.company.enums.ModeratorActionEnum.NOT_REVIEWED")
    Page<ArticleEntity> getArticlesForReview(PageRequest of);
    @Query("SELECT new com.company.mapper.ArticleShortViewInfo(a.uuid, a.title, a.description, a.publishedAt) " +
            "FROM ArticleEntity a " +
            "WHERE a.articleTypeId = ?1 AND a.articleStatus = com.company.enums.ArticleStatusEnum.PUBLISHED AND a.languageId = ?2 " +
            "ORDER BY a.createdAt DESC limit 5")
    List<IArticleShortViewInfo> findLastFiveByType(int typeId, int languageId);

    @Query(value = "select new com.company.mapper.ArticleShortViewInfo(a.uuid, a.title, a.description, a.publishedAt) from ArticleEntity as a " +
            "where a.articleStatus = com.company.enums.ArticleStatusEnum.PUBLISHED" +
            " and a.uuid not in(?1) and a.languageId = ?2 order by a.createdAt desc limit 8")
    List<IArticleShortViewInfo> getTop8ByArticleStatusAndUuidNotInOrderByCreatedAt(List<String> uuid, int languageId);

    Optional<ArticleEntity> findByUuidAndArticleStatus(String id, ArticleStatusEnum articleStatusEnum);
}
