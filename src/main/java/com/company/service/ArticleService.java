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
        if(articleDTO.getTitle().isEmpty() || articleDTO.getTitle().isBlank())
            throw new ArticleCreateException("Article Title should be filled");
        if(articleDTO.getContent().isEmpty() || articleDTO.getContent().isBlank())
            throw new ArticleCreateException("Article Content cannot be empty");
        if(articleDTO.getDescription().isEmpty() || articleDTO.getDescription().isBlank())
            throw new ArticleCreateException("Article description cannot be empty");
        articleDTO.setArticleStatus(ArticleStatusEnum.PUBLISHED);

        ArticleEntity article = toEntity(articleDTO);
        articleDTO.setCreatedAt(article.getCreatedAt());
        articleDTO.setPublishedAt(article.getPublishedAt());

        articleRepository.save(article);
        return articleDTO;
    }

    private ArticleEntity toEntity(ArticleDTO articleDTO){
        ArticleEntity article = new ArticleEntity();
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        article.setDescription(articleDTO.getDescription());
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
            articleDTO.setTitle(articleEntity.getTitle());
            articleDTO.setDescription(articleEntity.getDescription());
            articleDTO.setVisible(articleEntity.isVisible());
            articleDTO.setContent(articleEntity.getContent());
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

        article.setContent(articleDTO.getContent());
        article.setTitle(articleDTO.getTitle());
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
        articleDTO.setDescription(article.getDescription());
        articleDTO.setContent(article.getContent());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setVisible(article.isVisible());
        articleDTO.setPublishedAt(article.getPublishedAt());
        articleDTO.setCreatedAt(article.getCreatedAt());
        articleDTO.setCategoryId(article.getCategoryId());

        return articleDTO;
    }

    public Page<ArticleDTO> findArticlesOrderedByTitle(int page, int size) {
        PageRequest of = PageRequest.of(page, size);

        Page<ArticleEntity> articlesByTitle = articleRepository.findArticlesByTitle(of);

        Stream<ArticleEntity> articleEntityStream = articlesByTitle.get();

        List<ArticleDTO> articleDTOS = articleEntityStream.map(this::toDto).toList();

        long totalElements = articlesByTitle.getTotalElements();

        Page<ArticleDTO> response = new PageImpl<>(articleDTOS, of, totalElements);

        return response;
    }

    public List<ArticleDTO> searchArticlesByTitle(String title){
        title = "%" + title + "%";
        List<ArticleEntity> articleEntities = articleRepository.findArticleEntitiesByTitleLikeIgnoreCase(title);

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
