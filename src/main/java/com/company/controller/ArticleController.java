package com.company.controller;

import com.company.dto.ArticleDTO;
import com.company.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Article Controller is a Controller for Article entity manipulation.
 */

@RestController
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService){
        this.articleService = articleService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createPost(@RequestBody ArticleDTO articleDTO){
        ArticleDTO res = articleService.createPost(articleDTO);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/")
    public ResponseEntity<?> getArticlePagination(@RequestParam("page") int page, @RequestParam("size") int size){
        Page<ArticleDTO> getArticlePaginationList = articleService.getArticlePagination(page, size);

        return ResponseEntity.ok(getArticlePaginationList);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteById(@PathVariable String uuid){
        String response = articleService.deleteById(uuid);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<?> updateById(@PathVariable String uuid, @RequestBody ArticleDTO articleDTO){
        String response = articleService.updateById(uuid, articleDTO);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> getById(@PathVariable String uuid){
        ArticleDTO articleDTO = articleService.getById(uuid);

        return ResponseEntity.ok(articleDTO);
    }

    @GetMapping("/order/published-date/")
    public ResponseEntity<?> getArticlesOrderedByPublishedDate(@RequestParam("page") int page, @RequestParam("size") int size){
        Page<ArticleDTO> response = articleService.findAllArticleByPublishedDate(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/title")
    public ResponseEntity<?> getArticlesByOrderedByTitle(@RequestParam("page") int page, @RequestParam("size") int size){
        Page<ArticleDTO> response = articleService.findArticlesOrderedByTitleUz(page, size);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchArticlesByTitleUz(@RequestParam("title") String title){
        List<ArticleDTO> articleDTOS = articleService.searchArticlesByTitleUz(title);

        return ResponseEntity.ok(articleDTOS);
    }

    @GetMapping("/category/{key}")
    public ResponseEntity<?> getArticlesByCategory(@PathVariable String key,
                                                   @RequestParam("page") int page,
                                                   @RequestParam("size") int size){
        Page<ArticleDTO> articleDTOS = articleService.getArticlesByCategory(key, page, size);

        return ResponseEntity.ok(articleDTOS);
    }
}
