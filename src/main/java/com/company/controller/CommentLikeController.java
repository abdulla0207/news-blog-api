package com.company.controller;

import com.company.service.CommentLikeService;
import com.company.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/comment/like")
@Tag(name = "Comment Like API list")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    public CommentLikeController(CommentLikeService commentLikeService){
        this.commentLikeService = commentLikeService;
    }

    @Operation(summary = "Create like comment")
    @PostMapping("/")
    public ResponseEntity<?> likeComment(HttpServletRequest request,
                                         @RequestParam(name = "comment-id") String commentId,
                                         @RequestHeader("Accept-Language") String lang){
        log.info("create like for comment {}", commentId);
        Integer idFromHeader = JwtUtil.getIdFromHeader(request);
        String response = commentLikeService.likeComment(commentId, idFromHeader, lang);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create dislike comment")
    @PostMapping("/dislike")
    public ResponseEntity<?> dislikeComment(HttpServletRequest request, @RequestParam(name = "comment-id") String commentId,
                                            @RequestHeader("Accept-Language") String lang){
        log.info("create dislike for comment {}", commentId);
        Integer idFromHeader = JwtUtil.getIdFromHeader(request);
        String response = commentLikeService.dislikeComment(commentId, idFromHeader, lang);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete like or dislike comment")
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeLikeDislikeFromComment(HttpServletRequest request,
                                                          @RequestParam(name = "comment-id") String commentId,
                                                          @RequestHeader("Accept-Language") String lang){
        log.info("remove like or dislike {}", commentId);
        Integer idFromHeader = JwtUtil.getIdFromHeader(request);
        String response = commentLikeService.removeLikeDislikeFromComment(idFromHeader, commentId, lang);

        return ResponseEntity.ok(response);
    }
}
