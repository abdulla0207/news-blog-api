package com.company.controller;

import com.company.dto.article.ArticleLikeDTO;
import com.company.dto.article.ArticleShortDTO;
import com.company.service.ArticleLikeService;
import com.company.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/article/like")
public class ArticleLikeController {
    private final ArticleLikeService articleLikeService;

    @Autowired
    public ArticleLikeController(ArticleLikeService articleLikeService){
        this.articleLikeService = articleLikeService;
    }

    @PostMapping("/like")
    public ResponseEntity<?> likeArticle(HttpServletRequest request,
                                         @RequestParam(name = "article-id") String articleId){
        log.info("article is liked {}", articleId);
        Integer idFromHeader = JwtUtil.getIdFromHeader(request);
        String response = articleLikeService.likeArticle(articleId, idFromHeader);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/unlike")
    public ResponseEntity<?> unlikeArticle(HttpServletRequest request,
                                           @RequestParam(name = "article-id") String articleId){
        log.info("article disliked {}", articleId);
        Integer idFromHeader = JwtUtil.getIdFromHeader(request);
        String response = articleLikeService.dislikeArticle(articleId, idFromHeader);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeLikeDislikeFromArticle(HttpServletRequest request,
                                                          @RequestParam(name = "article-id") String articleId){
        log.info("article like or dislike removed");
        Integer idFromHeader = JwtUtil.getIdFromHeader(request);
        String response = articleLikeService.removeLikeDislikeFromArticle(idFromHeader, articleId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/hasLikes")
    public ResponseEntity<?> hasUserLikedOrDisliked(HttpServletRequest request,
                                                    @RequestParam(name = "article-id") String articleId){
        Integer idFromHeader = JwtUtil.getIdFromHeader(request);
        ArticleLikeDTO response = articleLikeService.hasUserLikedOrDisliked(articleId, idFromHeader);

        return ResponseEntity.ok(response);
    }


}
