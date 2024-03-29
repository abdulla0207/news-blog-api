package com.company.service;

import com.company.dto.comment.CommentDTO;
import com.company.dto.comment.CommentFullDTO;
import com.company.dto.comment.CommentReplyDTO;
import com.company.dto.comment.ProfileForCommentDTO;
import com.company.entity.CommentEntity;
import com.company.enums.ProfileRoleEnum;
import com.company.exception.AppForbiddenException;
import com.company.exception.ItemNotFoundException;
import com.company.mapper.CommentFullInfoMapper;
import com.company.mapper.ICommentFullInfoMapper;
import com.company.repository.CommentRepository;
import com.company.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ResourceMessageService resourceMessageService;

    @Autowired
    public CommentService(CommentRepository commentRepository, ResourceMessageService resourceMessageService){
        this.commentRepository = commentRepository;
        this.resourceMessageService = resourceMessageService;
    }

    public CommentDTO createComment(Integer idFromHeader, String articleId, CommentDTO commentDTO) {
        CommentEntity entity = new CommentEntity();

        entity.setCreatedAt(LocalDateTime.now());
        entity.setUserId(idFromHeader);
        entity.setContent(commentDTO.content());
        entity.setArticleId(articleId);
        entity.setVisible(true);

        commentRepository.save(entity);

        return new CommentDTO(entity.getUuid(), entity.getContent(), articleId, idFromHeader, entity.getCreatedAt(), null, true);
    }


    public CommentDTO updateComment(Integer userId, CommentDTO commentDTO, String commentId, String lang) {
        Optional<CommentEntity> byId = commentRepository.findById(commentId);

        if(byId.isEmpty()) {
            log.warn("Comment not found {}", commentId);
            throw new ItemNotFoundException(resourceMessageService.getMessage("comment.not.found", lang));
        }

        CommentEntity entity = byId.get();

        if(!entity.getUserId().equals(userId)) {
            log.warn("Wrong user tried to update comment {}", userId);
            throw new AppForbiddenException(resourceMessageService.getMessage("method.not.allowed", lang));
        }

        entity.setUpdatedAt(LocalDateTime.now());
        entity.setContent(commentDTO.content());

        commentRepository.save(entity);

        CommentDTO response = new CommentDTO(entity.getUuid(), entity.getContent(), entity.getArticleId(),
                entity.getUserId(), entity.getCreatedAt(), entity.getUpdatedAt(), entity.isVisible());

        return response;
    }

    @Transactional
    @Modifying
    public String deleteById(String commentId, HttpServletRequest request, String lang) {
        Optional<CommentEntity> byId = commentRepository.findById(commentId);
        Integer commentUserId = JwtUtil.getIdFromHeader(request);

        if(byId.isEmpty()) {
            log.warn("Comment not found with id {}", commentId);
            throw new ItemNotFoundException(resourceMessageService.getMessage("comment.not.found", lang));
        }
        CommentEntity entity = byId.get();

        if(!entity.getUserId().equals(commentUserId)) {
            log.warn("Wrong user tried to delete comment {}", commentUserId);
            throw new AppForbiddenException(resourceMessageService.getMessage("method.not.allowed", lang));
        }

        commentRepository.deleteById(commentId);

        return resourceMessageService.getMessage("comment.deleted", lang);
    }

    public Page<CommentFullDTO> getCommentsForArticle(String articleId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ICommentFullInfoMapper> entities = commentRepository.findAllByArticleId(articleId, pageable);

        Stream<ICommentFullInfoMapper> streamEntities = entities.get();

        List<CommentFullDTO> dtos = streamEntities.map(this::toDto).toList();
        long totalElements = entities.getTotalElements();

        return new PageImpl<>(dtos, pageable, totalElements);
    }

    private CommentFullDTO toDto(ICommentFullInfoMapper entity){
        ProfileForCommentDTO profile = new ProfileForCommentDTO(entity.getUserId(), entity.getUserName(), entity.getUserSurname());
        CommentFullDTO commentFullDTO = new CommentFullDTO(entity.getUuid(), entity.getContent(),
                entity.getCreatedAt(), entity.getUpdatedAt(), profile);

        return commentFullDTO;
    }

    public List<CommentFullDTO> getRepliedCommentsForComment(String commentId) {
        List<ICommentFullInfoMapper> entities = commentRepository.findRepliedCommentsForComment(commentId);

        List<CommentFullDTO> response = entities.stream().map(this::toDto).toList();

        return response;
    }

    public CommentReplyDTO replyComment(String parentId, Integer userId, CommentDTO commentDTO, String articleId) {
        CommentEntity entity = new CommentEntity();

        entity.setContent(commentDTO.content());
        entity.setParentCommentId(parentId);
        entity.setUserId(userId);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setVisible(true);
        entity.setArticleId(articleId);

        commentRepository.save(entity);

        return new CommentReplyDTO(entity.getUuid(), entity.getContent(), entity.getArticleId(), entity.getUserId(), entity.getCreatedAt(), entity.getParentCommentId());
    }
}
