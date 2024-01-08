package com.company.repository;

import com.company.entity.ArticleTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ArticleTypeRepository extends JpaRepository<ArticleTypeEntity, Integer> {

}
