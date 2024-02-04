package com.company.service;

import com.company.dto.article.ArticleCreateDTO;
import com.company.dto.article.ArticleDTO;
import com.company.dto.article.ArticleShortDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.CategoryEntity;
import com.company.enums.ArticleStatusEnum;
import com.company.enums.LikeStatusEnum;
import com.company.enums.ModeratorActionEnum;
import com.company.exception.AppForbiddenException;
import com.company.exception.ItemNotFoundException;
import com.company.mapper.IArticleShortViewInfo;
import com.company.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    private final CategoryService categoryService;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, CategoryService categoryService){
        System.out.println("In constr" + getClass().getSimpleName());
        this.articleRepository = articleRepository;
        this.categoryService = categoryService;
    }
    public ArticleDTO createPost(ArticleCreateDTO articleCreateDTO, int writerId) {
        ArticleEntity article = toEntity(articleCreateDTO);
        article.setAuthorId(writerId);
        article.setModeratorAction(ModeratorActionEnum.NOT_REVIEWED);
        ArticleDTO resDTO = toDto(article);
        articleRepository.save(article);
        return resDTO;
    }

    public Page<ArticleDTO> getArticlePagination(String languageCode, int page, int size) {
        int languageId = getLanguageIdByCode(languageCode);
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleEntity> pageAllArticle =articleRepository.findAllByLanguageCode(languageId, pageable);

        return returnPagination(pageAllArticle, pageable);
    }

    private int getLanguageIdByCode(String languageCode) {
        if("uz".equalsIgnoreCase(languageCode))
            return 1;
        else if("en".equalsIgnoreCase(languageCode))
            return 2;
        else
            throw new IllegalArgumentException("Unsupported Language Code");
    }

    public Page<ArticleDTO> getArticlesForPublish(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<ArticleEntity> articlesForReview = articleRepository.getArticlesForPublish(pageRequest);

        Stream<ArticleEntity> articleEntityStream = articlesForReview.get();
        long totalElements = articlesForReview.getTotalElements();
        List<ArticleDTO> responseList = articleEntityStream.map(this::toDto).toList();

        return new PageImpl<>(responseList, pageRequest, totalElements);
    }

    public Page<ArticleDTO> findAllArticleByPublishedDate(int page, int size, String languageCode){
        Pageable pageable = PageRequest.of(page, size);

        int languageId = getLanguageIdByCode(languageCode);
        Page<ArticleEntity> articlesOrderByPublishedDate = articleRepository.findArticlesByPublishedDate(languageId, pageable);

        return returnPagination(articlesOrderByPublishedDate, pageable);
    }

    public String deleteById(String uuid) {
        Optional<ArticleEntity> findById = articleRepository.findById(uuid);

        if(findById.isEmpty())
            throw new ItemNotFoundException("Article with the ID not found in DB");

        articleRepository.deleteById(uuid);
        return "Article deleted";
    }

    @Transactional
    public String updateById(String uuid, ArticleCreateDTO articleDTO, Integer currentUserId) {
        Optional<ArticleEntity> findById = articleRepository.findById(uuid);

        if(findById.isEmpty())
            throw new ItemNotFoundException("Article not found");

        ArticleEntity article = findById.get();

        if(!article.getAuthorId().equals(currentUserId))
            throw new AppForbiddenException("Method not allowed");

        article.setContent(articleDTO.content());
        article.setTitle(articleDTO.title());
        article.setDescription(articleDTO.description());
        article.setRegionId(articleDTO.regionId());
        article.setCategoryId(articleDTO.categoryId());
        article.setLanguageId(articleDTO.languageId());
        article.setArticleTypeId(articleDTO.articleTypeId());
        article.setModeratorAction(ModeratorActionEnum.NOT_REVIEWED);
        article.setArticleStatus(ArticleStatusEnum.NOT_PUBLISHED);

        articleRepository.save(article);

        return "Article Updated";
    }

    public ArticleDTO getById(String uuid) {
        Optional<ArticleEntity> getById = articleRepository.findByUuidAndArticleStatus(uuid, ArticleStatusEnum.PUBLISHED);

        if(getById.isEmpty())
            throw new ItemNotFoundException("Article not FOUND");

        ArticleEntity article = getById.get();

        return toDto(article);
    }

    public Page<ArticleDTO> findArticlesOrderedByTitle(int page, int size, String languageCode) {
        PageRequest of = PageRequest.of(page, size);

        int languageId = getLanguageIdByCode(languageCode);
        Page<ArticleEntity> articlesByTitle = articleRepository.findArticlesByTitle(of, languageId);


        return returnPagination(articlesByTitle, of);
    }

    public List<ArticleDTO> searchArticlesByTitle(String title, String languageCode){
        int languageId = getLanguageIdByCode(languageCode);
        title = "%" + title + "%";
        List<ArticleEntity> articleEntities = articleRepository.findArticleEntitiesByTitleLikeIgnoreCaseAndLanguageIdAndArticleStatus(title, languageId, ArticleStatusEnum.PUBLISHED);

        return toDtoList(articleEntities);
    }

    public Page<ArticleDTO> getArticlesByCategory(String key, int page, int size, String language) {
        int languageId = getLanguageIdByCode(language);
        CategoryEntity category = categoryService.getCategoryByKey(key);

        PageRequest pageable = PageRequest.of(page, size);

        Page<ArticleEntity> articleEntities = articleRepository.findArticleEntitiesByCategory_KeyAndLanguageId(pageable, category.getKey(), languageId);

        return returnPagination(articleEntities, pageable);
    }


    public String changeStatus(String id, Integer publisherId, ArticleStatusEnum statusEnum) {
        Optional<ArticleEntity> byId = articleRepository.findById(id);

        if(byId.isEmpty())
            throw new ItemNotFoundException("Article not found. Wrong ID");

        ArticleEntity articleEntity = byId.get();

        articleEntity.setArticleStatus(statusEnum);
        articleEntity.setPublisherId(publisherId);
        articleEntity.setPublishedAt(LocalDateTime.now());

        articleRepository.save(articleEntity);

        return "Article Published";
    }

    public Page<ArticleDTO> getArticleForReview(int page, int size) {
        PageRequest of = PageRequest.of(page, size);

        Page<ArticleEntity> articlesByTitle = articleRepository.getArticlesForReview(of);

        return returnPagination(articlesByTitle, of);
    }

    public String updateModeratorAction(String articleId, ModeratorActionEnum moderatorAction, Integer moderatorId) {
        Optional<ArticleEntity> byId = articleRepository.findById(articleId);
        if (byId.isEmpty())
            throw new ItemNotFoundException("Article Not Found");

        ArticleEntity articleEntity = byId.get();

        articleEntity.setModeratorAction(moderatorAction);
        articleEntity.setModeratorId(moderatorId);

        articleRepository.save(articleEntity);

        return "Article Updated";
    }

    public List<ArticleShortDTO> getLastFiveByType(int typeId, String language){
        int languageId = getLanguageIdByCode(language);
        List<IArticleShortViewInfo> entities = articleRepository.findLastFiveByType(typeId, languageId);

        if(entities.isEmpty())
            throw new ItemNotFoundException("Articles Not Found");
        List<ArticleShortDTO> response = new ArrayList<>();
        for(IArticleShortViewInfo entity : entities){
            ArticleShortDTO holder = new ArticleShortDTO(entity.getUuid(), entity.getTitle(), entity.getDescription(), entity.getPublishedDate());
            response.add(holder);
        }

        return response;
    }
    public List<ArticleShortDTO> getLastEightNotIncludeId(List<String> uuid, String language) {
        List<IArticleShortViewInfo> entities = articleRepository.getTop8ByArticleStatusAndUuidNotInOrderByCreatedAt(uuid, getLanguageIdByCode(language));

        List<ArticleShortDTO> response = new ArrayList<>();

        entities.forEach(entity ->{
            ArticleShortDTO holder = new ArticleShortDTO(entity.getUuid(), entity.getTitle(), entity.getDescription(), entity.getPublishedDate());
            response.add(holder);
        });

        return response;
    }

    private Page<ArticleDTO> returnPagination(Page<ArticleEntity> entities, Pageable of){
        Stream<ArticleEntity> articleEntityStream = entities.get();

        List<ArticleDTO> articleDTOS = articleEntityStream.map(this::toDto).toList();

        long totalElements = entities.getTotalElements();

        return new PageImpl<>(articleDTOS, of, totalElements);
    }

    private ArticleDTO toDto(ArticleEntity article) {
        return new ArticleDTO(
                article.getUuid(),
                article.getTitle(),
                article.getDescription(),
                article.getContent(),
                article.getArticleStatus(),
                article.getCreatedAt(),
                article.getPublishedAt(),
                article.isVisible(),
                article.getCategoryId(),
                article.getRegionId(),
                article.getLanguageId(),
                article.getArticleTypeId()
        );
    }

    private List<ArticleDTO> toDtoList(List<ArticleEntity> articleEntities){
        List<ArticleDTO> res = new ArrayList<>();
        ArticleDTO articleDTO = null;
        for (ArticleEntity articleEntity : articleEntities) {
            articleDTO = toDto(articleEntity);
            res.add(articleDTO);
        }

        return res;
    }

    private ArticleEntity toEntity(ArticleCreateDTO articleDTO){
        ArticleEntity article = new ArticleEntity();
        article.setTitle(articleDTO.title());
        article.setContent(articleDTO.content());
        article.setDescription(articleDTO.description());
        article.setArticleStatus(ArticleStatusEnum.NOT_PUBLISHED);
        article.setVisible(true);
        article.setCreatedAt(LocalDateTime.now());
        article.setCategoryId(articleDTO.categoryId());
        article.setLanguageId(articleDTO.languageId());
        article.setArticleTypeId(articleDTO.articleTypeId());
        article.setRegionId(articleDTO.regionId());
        return article;
    }

    public List<ArticleEntity> findArticlesByUserAndStatus(Integer idFromHeader, LikeStatusEnum likeStatusEnum) {
        List<ArticleEntity> entities = articleRepository.findArticlesByUserAndStatus(idFromHeader, likeStatusEnum);

        return entities;
    }
}
