package com.company.controller;

import com.company.dto.comment.CommentDTO;
import com.company.dto.comment.CommentFullDTO;
import com.company.service.CommentService;
import com.company.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping("/{articleId}")
    public ResponseEntity<CommentDTO> createComment(HttpServletRequest request, @Valid @RequestBody CommentDTO commentDTO, @PathVariable(name = "articleId") String articleId){
        Integer idFromHeader = JwtUtil.getIdFromHeader(request);

        CommentDTO response = commentService.createComment(idFromHeader, articleId, commentDTO);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(HttpServletRequest request, @Valid @RequestBody CommentDTO commentDTO, @PathVariable(name = "commentId") String commentId){

        Integer userId = JwtUtil.getIdFromHeader(request);

        CommentDTO response = commentService.updateComment(userId, commentDTO, commentId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{commentId}")
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
}
