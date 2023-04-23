package com.company.service;

import com.company.dto.ArticleDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.CategoryEntity;
import com.company.enums.ArticleStatusEnum;
import com.company.exception.ArticleCreateException;
import com.company.exception.ItemNotFoundException;
import com.company.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    private final CategoryService categoryService;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, CategoryService categoryService){
        this.articleRepository = articleRepository;
        this.categoryService = categoryService;
    }
    public ArticleDTO createPost(ArticleDTO articleDTO) {
        if(articleDTO.getTitleUz().isEmpty() || articleDTO.getTitleUz().isBlank())
            throw new ArticleCreateException("Article Title in Uzbek should be filled");
        if(articleDTO.getTitleEn().isEmpty() || articleDTO.getTitleEn().isBlank())
            throw new ArticleCreateException("Article Title in English should be filled");

        if(articleDTO.getContentUz().isEmpty() || articleDTO.getContentUz().isBlank())
            throw new ArticleCreateException("Article Content in Uzbek cannot be empty");
        if(articleDTO.getContentEn().isEmpty() || articleDTO.getContentEn().isBlank())
            throw new ArticleCreateException("Article Content in English cannot be empty");

        if(articleDTO.getDescriptionUz().isEmpty() || articleDTO.getDescriptionUz().isBlank())
            throw new ArticleCreateException("Article description in Uzbek cannot be empty");
        if(articleDTO.getDescriptionEn().isEmpty() || articleDTO.getDescriptionEn().isBlank())
            throw new ArticleCreateException("Article description in English cannot be empty");
        articleDTO.setArticleStatus(ArticleStatusEnum.PUBLISHED);

        ArticleEntity article = toEntity(articleDTO);
        articleDTO.setCreatedAt(article.getCreatedAt());
        articleDTO.setPublishedAt(article.getPublishedAt());

        articleRepository.save(article);
        return articleDTO;
    }

    private ArticleEntity toEntity(ArticleDTO articleDTO){
        ArticleEntity article = new ArticleEntity();
        article.setTitleUz(articleDTO.getTitleUz());
        article.setTitleEn(articleDTO.getTitleEn());
        article.setContentUz(articleDTO.getContentUz());
        article.setContentEn(articleDTO.getContentEn());
        article.setDescriptionUz(articleDTO.getDescriptionUz());
        article.setDescriptionEn(articleDTO.getDescriptionEn());
        article.setArticleStatus(ArticleStatusEnum.NOT_PUBLISHED);
        article.setVisible(true);
        article.setCreatedAt(LocalDateTime.now());
        article.setPublishedAt(LocalDateTime.now());
        article.setCategoryId(articleDTO.getCategoryId());

        return article;
    }

    public Page<ArticleDTO> getArticlePagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleEntity> pageAllArticle =articleRepository.findAll(pageable);

        List<ArticleEntity> content = pageAllArticle.getContent();
        long totalElements = pageAllArticle.getTotalElements();

        List<ArticleDTO> articleDTOS = toDtoList(content);

        Page<ArticleDTO> res = new PageImpl<>(articleDTOS, pageable, totalElements);

        return res;
    }

    private List<ArticleDTO> toDtoList(List<ArticleEntity> articleEntities){
        List<ArticleDTO> res = new ArrayList<>();
        ArticleDTO articleDTO = null;
        for (ArticleEntity articleEntity : articleEntities) {
            articleDTO = new ArticleDTO();
            articleDTO.setUuid(articleEntity.getUuid());
            articleDTO.setTitleUz(articleEntity.getTitleUz());
            articleDTO.setTitleEn(articleEntity.getTitleEn());
            articleDTO.setDescriptionUz(articleEntity.getDescriptionUz());
            articleDTO.setDescriptionEn(articleEntity.getDescriptionEn());
            articleDTO.setVisible(articleEntity.isVisible());
            articleDTO.setContentUz(articleEntity.getContentUz());
            articleDTO.setContentEn(articleEntity.getContentEn());
            articleDTO.setArticleStatus(articleEntity.getArticleStatus());
            articleDTO.setPublishedAt(articleEntity.getPublishedAt());
            articleDTO.setCreatedAt(articleEntity.getCreatedAt());

            res.add(articleDTO);
        }

        return res;
    }

    public Page<ArticleDTO> findAllArticleByPublishedDate(int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        Page<ArticleEntity> articlesOrderByPublishedDate = articleRepository.findArticlesByPublishedDate(pageable);

        Stream<ArticleEntity> articleEntityStream = articlesOrderByPublishedDate.get();
        long totalElements = articlesOrderByPublishedDate.getTotalElements();
        List<ArticleDTO> response = articleEntityStream.map(this::toDto).toList();

        Page<ArticleDTO> articleDTOS = new PageImpl<>(response, pageable, totalElements);

        return articleDTOS;
    }

    public String deleteById(String uuid) {
        Optional<ArticleEntity> findById = articleRepository.findById(uuid);

        if(findById.isEmpty())
            throw new ItemNotFoundException("Article with the ID not found in DB");

        articleRepository.deleteById(uuid);
        return "Article deleted";
    }

    public String updateById(String uuid, ArticleDTO articleDTO) {
        Optional<ArticleEntity> findById = articleRepository.findById(uuid);

        if(findById.isEmpty())
            throw new ItemNotFoundException("Article not found");

        ArticleEntity article = findById.get();

        article.setContentUz(articleDTO.getContentUz());
        article.setContentEn(articleDTO.getContentEn());
        article.setTitleUz(articleDTO.getTitleUz());
        article.setTitleEn(articleDTO.getTitleEn());
        article.setDescriptionEn(articleDTO.getDescriptionEn());
        article.setDescriptionUz(articleDTO.getDescriptionUz());
        article.setArticleStatus(articleDTO.getArticleStatus());
        article.setVisible(articleDTO.isVisible());

        articleRepository.save(article);
        articleDTO.setUuid(article.getUuid());

        return "Article Updated";
    }

    public ArticleDTO getById(String uuid) {
        Optional<ArticleEntity> getById = articleRepository.findById(uuid);

        if(getById.isEmpty())
            throw new ItemNotFoundException("Article not FOUND");

        ArticleEntity article = getById.get();

        return toDto(article);
    }

    private ArticleDTO toDto(ArticleEntity article) {
        ArticleDTO articleDTO = new ArticleDTO();

        articleDTO.setUuid(article.getUuid());
        articleDTO.setDescriptionUz(article.getDescriptionUz());
        articleDTO.setDescriptionEn(article.getDescriptionEn());
        articleDTO.setContentUz(article.getContentUz());
        articleDTO.setContentEn(article.getContentEn());
        articleDTO.setTitleUz(article.getTitleUz());
        articleDTO.setTitleEn(article.getTitleEn());
        articleDTO.setVisible(article.isVisible());
        articleDTO.setPublishedAt(article.getPublishedAt());
        articleDTO.setCreatedAt(article.getCreatedAt());
        articleDTO.setCategoryId(article.getCategoryId());

        return articleDTO;
    }

    public Page<ArticleDTO> findArticlesOrderedByTitleUz(int page, int size) {
        PageRequest of = PageRequest.of(page, size);

        Page<ArticleEntity> articlesByTitle = articleRepository.findArticlesByTitle(of);

        Stream<ArticleEntity> articleEntityStream = articlesByTitle.get();

        List<ArticleDTO> articleDTOS = articleEntityStream.map(this::toDto).toList();

        long totalElements = articlesByTitle.getTotalElements();

        Page<ArticleDTO> response = new PageImpl<>(articleDTOS, of, totalElements);

        return response;
    }

    public List<ArticleDTO> searchArticlesByTitleUz(String titleUz){
        titleUz = "%" + titleUz + "%";
        List<ArticleEntity> articleEntities = articleRepository.findArticleEntitiesByTitleUzLikeIgnoreCase(titleUz);

        return toDtoList(articleEntities);
    }

    public Page<ArticleDTO> getArticlesByCategory(String key, int page, int size) {
        CategoryEntity category = categoryService.getCategoryByKey(key);

        Pageable pageable = PageRequest.of(page, size);

        Page<ArticleEntity> articleEntities = articleRepository.findArticleEntitiesByCategory_Key(pageable, category.getKey());

        long totalElements = articleEntities.getTotalElements();

        Stream<ArticleEntity> articleEntityStream = articleEntities.get();


        List<ArticleDTO> responseDTO = articleEntityStream.map(this::toDto).toList();

        Page<ArticleDTO> responsePage = new PageImpl<>(responseDTO, pageable, totalElements);

        return responsePage;
    }
}
