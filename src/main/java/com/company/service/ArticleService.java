package com.company.service;

import com.company.dto.ArticleDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.CategoryEntity;
import com.company.enums.ArticleStatusEnum;
import com.company.exception.ArticleCreateException;
import com.company.exception.ItemNotFoundException;
import com.company.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
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
        System.out.println("In constr" + getClass().getSimpleName());
        this.articleRepository = articleRepository;
        this.categoryService = categoryService;
    }
    public ArticleDTO createPost(ArticleDTO articleDTO) {
        if(articleDTO.titleUz().isEmpty() || articleDTO.titleUz().isBlank())
            throw new ArticleCreateException("Article Title in Uzbek should be filled");
        if(articleDTO.titleEn().isEmpty() || articleDTO.titleEn().isBlank())
            throw new ArticleCreateException("Article Title in English should be filled");

        if(articleDTO.contentUz().isEmpty() || articleDTO.contentUz().isBlank())
            throw new ArticleCreateException("Article Content in Uzbek cannot be empty");
        if(articleDTO.contentEn().isEmpty() || articleDTO.contentEn().isBlank())
            throw new ArticleCreateException("Article Content in English cannot be empty");

        if(articleDTO.descriptionUz().isEmpty() || articleDTO.descriptionUz().isBlank())
            throw new ArticleCreateException("Article description in Uzbek cannot be empty");
        if(articleDTO.descriptionEn().isEmpty() || articleDTO.descriptionEn().isBlank())
            throw new ArticleCreateException("Article description in English cannot be empty");

        ArticleEntity article = toEntity(articleDTO);

        ArticleDTO resDTO = toDto(article);
        articleRepository.save(article);
        return resDTO;
    }

    private ArticleEntity toEntity(ArticleDTO articleDTO){
        ArticleEntity article = new ArticleEntity();
        article.setTitleUz(articleDTO.titleUz());
        article.setTitleEn(articleDTO.titleEn());
        article.setContentUz(articleDTO.contentUz());
        article.setContentEn(articleDTO.contentEn());
        article.setDescriptionUz(articleDTO.descriptionUz());
        article.setDescriptionEn(articleDTO.descriptionEn());
        article.setArticleStatus(ArticleStatusEnum.NOT_PUBLISHED);
        article.setVisible(true);
        article.setCreatedAt(LocalDateTime.now());
        article.setPublishedAt(LocalDateTime.now());
        article.setCategoryId(articleDTO.categoryId());

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
            articleDTO = toDto(articleEntity);
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

        article.setContentUz(articleDTO.contentUz());
        article.setContentEn(articleDTO.contentEn());
        article.setTitleUz(articleDTO.titleUz());
        article.setTitleEn(articleDTO.titleEn());
        article.setDescriptionEn(articleDTO.descriptionEn());
        article.setDescriptionUz(articleDTO.descriptionUz());
        article.setArticleStatus(articleDTO.articleStatus());
        article.setVisible(articleDTO.visible());

        articleRepository.save(article);

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
        ArticleDTO articleDTO = new ArticleDTO(
                article.getUuid(),
                article.getTitleUz(),
                article.getTitleEn(),
                article.getDescriptionUz(),
                article.getDescriptionEn(),
                article.getContentUz(),
                article.getContentEn(),
                article.getArticleStatus(),
                article.getCreatedAt(),
                article.getPublishedAt(),
                article.isVisible(),
                article.getCategoryId()
        );
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
