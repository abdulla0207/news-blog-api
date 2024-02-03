package com.company.repository;

import com.company.entity.CommentEntity;
import com.company.mapper.CommentFullInfoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, String> {

    @Query(value = "select c.uuid as uuid, c.content as content, c.created_at as createdAt, c.updated_at as updatedAt, " +
            "u.id as userId, u.name as userName, u.surname as userSurname from comment as c " +
            "join profile as u on c.user_id = u.id where c.uuid = ?1 order by c.created_at", nativeQuery = true)
    Page<CommentFullInfoMapper> findAllByArticleId(String articleId, Pageable pageable);
}
