package com.company.service;

import com.company.dto.ArticleDTO;
import com.company.entity.ArticleEntity;
import com.company.enums.ArticleStatusEnum;
import com.company.exception.ArticleCreateException;
import com.company.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {
    private ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository){
        this.articleRepository = articleRepository;
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
        article.setArticleStatus(articleDTO.getArticleStatus());
        article.setVisible(true);
        article.setCreatedAt(LocalDateTime.now());
        article.setPublishedAt(LocalDateTime.now());

        return article;
    }

    public Page<ArticleDTO> getArticlePagination(int page) {
        Pageable pageable = PageRequest.of(page, 5);
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
}
