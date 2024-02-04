package com.company.repository;

import com.company.entity.CommentEntity;
import com.company.mapper.CommentFullInfoMapper;
import com.company.mapper.ICommentFullInfoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, String> {

    @Query(value = "select c.uuid as uuid, c.content as content, c.created_at as createdAt, c.updated_at as updatedAt, " +
            "u.id as userId, u.name as userName, u.surname as userSurname from comment as c " +
            "join profile as u on c.user_id = u.id where c.uuid = ?1 order by c.created_at desc", nativeQuery = true)
    Page<ICommentFullInfoMapper> findAllByArticleId(String articleId, Pageable pageable);

//    @Query(value = "select c.uuid as uuid, c.content as content, c.created_at as createdAt, c.updated_at as updatedAt, " +
//            "u.id as userId, u.name as userName, u.surname as userSurname from comment as c " +
//            "join profile as u on c.user_id = u.id where c.parent_comment_id = ?1 order by c.created_at desc", nativeQuery = true)
//    List<CommentFullInfoMapper> findRepliedCommentsForComment(String commentId);
    @Query(value = "SELECT new com.company.mapper.CommentFullInfoMapper(c.uuid, c.content, c.createdAt, c.updatedAt, u.id, u.name, u.surname) from" +
        " CommentEntity c JOIN ProfileEntity u ON c.userId = u.id WHERE c.parentCommentId = ?1 ORDER BY c.createdAt DESC")
    List<ICommentFullInfoMapper> findRepliedCommentsForComment(String commentId);
}
