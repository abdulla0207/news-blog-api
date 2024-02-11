package com.company.controller;

import com.company.dto.comment.CommentDTO;
import com.company.dto.comment.CommentFullDTO;
import com.company.dto.comment.CommentReplyDTO;
import com.company.service.CommentService;
import com.company.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/comment")
@Tag(name = "Comment API list")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @Operation(summary = "Create comment for an article", description = "This endpoint creates a comment for an article. Needs article ID, user should be logged in, and comment body")
    @PostMapping("/user/{articleId}")
    public ResponseEntity<CommentDTO> createComment(HttpServletRequest request, @Valid @RequestBody CommentDTO commentDTO, @PathVariable(name = "articleId") String articleId){
        log.info("Create Comment {}", commentDTO);
        Integer idFromHeader = JwtUtil.getIdFromHeader(request);

        CommentDTO response = commentService.createComment(idFromHeader, articleId, commentDTO);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Reply to a comment in article")
    @PostMapping("/user/reply/{parentId}/{articleId}")
    public ResponseEntity<CommentReplyDTO> replyComment(@PathVariable(name = "parentId") String parentId, @Valid @RequestBody CommentDTO commentDTO, HttpServletRequest request,
                                                            @PathVariable(name = "articleId") String articleId){
        log.info("Reply to Comment {}", commentDTO);
        Integer userId = JwtUtil.getIdFromHeader(request);
        CommentReplyDTO response = commentService.replyComment(parentId, userId, commentDTO, articleId);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a comment")
    @PutMapping("/user/update/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(HttpServletRequest request, @Valid @RequestBody CommentDTO commentDTO, @PathVariable(name = "commentId") String commentId,
                                                    @RequestHeader("Accept-Language") String lang){
        log.info("Update comment content {}", commentDTO);
        Integer userId = JwtUtil.getIdFromHeader(request);

        CommentDTO response = commentService.updateComment(userId, commentDTO, commentId, lang);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a comment by ID")
    @DeleteMapping("/user/delete/{commentId}")
    public ResponseEntity<String> deleteById(HttpServletRequest request,
                                        @PathVariable(name = "commentId") String commentId,
                                             @RequestHeader("Accept-Language") String lang){
        log.info("delete comment {}", commentId);
        String response = commentService.deleteById(commentId, request, lang);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get comment list for article")
    @GetMapping("/{articleId}")
    public ResponseEntity<Page<CommentFullDTO>> getCommentsForSpecificArticle(@PathVariable(name = "articleId") String articleId, @RequestParam(name = "page") int page,
                                                                              @RequestParam(name = "size") int size){
        log.info("get comments for article");
        Page<CommentFullDTO> response = commentService.getCommentsForArticle(articleId, page, size);

        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Get replied comment list for specific comment")
    @GetMapping("/replied/comments/{commentId}")
    public ResponseEntity<?> getRepliedCommentsForComment(@PathVariable(name = "commentId") String commentId){
        log.info("get replied comments for comment");
        List<CommentFullDTO> response = commentService.getRepliedCommentsForComment(commentId);

        return ResponseEntity.ok(response);
    }
}
