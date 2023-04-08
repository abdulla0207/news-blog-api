package com.company.controller;

import com.company.dto.ArticleDTO;
import com.company.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {
    private ArticleService articleService;

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
    public ResponseEntity<?> getArticlePagination(@RequestParam("page") int page){
        Page<ArticleDTO> getArticlePaginationList = articleService.getArticlePagination(page);

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
}
