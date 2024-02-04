package com.company.controller;

import com.company.service.CommentLikeService;
import com.company.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment/like")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    public CommentLikeController(CommentLikeService commentLikeService){
        this.commentLikeService = commentLikeService;
    }

    @PostMapping("/")
    public ResponseEntity<?> likeArticle(HttpServletRequest request,
                                         @RequestParam(name = "comment-id") String commentId){
        Integer idFromHeader = JwtUtil.getIdFromHeader(request);
        String response = commentLikeService.likeComment(commentId, idFromHeader);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/dislike")
    public ResponseEntity<?> dislikeArticle(HttpServletRequest request, @RequestParam(name = "comment-id") String commentId){
        Integer idFromHeader = JwtUtil.getIdFromHeader(request);
        String response = commentLikeService.dislikeComment(commentId, idFromHeader);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeLikeDislikeFromComment(HttpServletRequest request,
                                                          @RequestParam(name = "comment-id") String commentId){
        Integer idFromHeader = JwtUtil.getIdFromHeader(request);
        String response = commentLikeService.removeLikeDislikeFromComment(idFromHeader, commentId);

        return ResponseEntity.ok(response);
    }
}
