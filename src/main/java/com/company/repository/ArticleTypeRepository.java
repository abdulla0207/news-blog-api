package com.company.repository;

import com.company.entity.ArticleTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ArticleTypeRepository extends JpaRepository<ArticleTypeEntity, Integer> {

    @Query("select a from ArticleTypeEntity as a where a.visible=true")
    List<ArticleTypeEntity> findAllWhereVisibleIsTrue();
}
