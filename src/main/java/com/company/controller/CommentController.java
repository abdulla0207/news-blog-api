package com.company.controller;

import com.company.dto.comment.CommentDTO;
import com.company.dto.comment.CommentFullDTO;
import com.company.dto.comment.CommentReplyDTO;
import com.company.service.CommentService;
import com.company.util.JwtUtil;
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
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping("/user/{articleId}")
    public ResponseEntity<CommentDTO> createComment(HttpServletRequest request, @Valid @RequestBody CommentDTO commentDTO, @PathVariable(name = "articleId") String articleId){
        Integer idFromHeader = JwtUtil.getIdFromHeader(request);

        CommentDTO response = commentService.createComment(idFromHeader, articleId, commentDTO);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/user/reply/{parentId}/{articleId}")
    public ResponseEntity<CommentReplyDTO> replyComment(@PathVariable(name = "parentId") String parentId, @Valid @RequestBody CommentDTO commentDTO, HttpServletRequest request,
                                                            @PathVariable(name = "articleId") String articleId){
        Integer userId = JwtUtil.getIdFromHeader(request);
        CommentReplyDTO response = commentService.replyComment(parentId, userId, commentDTO, articleId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/user/update/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(HttpServletRequest request, @Valid @RequestBody CommentDTO commentDTO, @PathVariable(name = "commentId") String commentId){

        Integer userId = JwtUtil.getIdFromHeader(request);

        CommentDTO response = commentService.updateComment(userId, commentDTO, commentId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/user/delete/{commentId}")
    public ResponseEntity<String> deleteById(HttpServletRequest request,
                                        @PathVariable(name = "commentId") String commentId){

        String response = commentService.deleteById(commentId, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<Page<CommentFullDTO>> getCommentsForSpecificArticle(@PathVariable(name = "articleId") String articleId, @RequestParam(name = "page") int page,
                                                                              @RequestParam(name = "size") int size){
        Page<CommentFullDTO> response = commentService.getCommentsForArticle(articleId, page, size);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/replied/comments/{commentId}")
    public ResponseEntity<?> getRepliedCommentsForComment(@PathVariable(name = "commentId") String commentId){
        List<CommentFullDTO> response = commentService.getRepliedCommentsForComment(commentId);

        return ResponseEntity.ok(response);
    }
}
