package com.company.repository;

import com.company.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {

    Optional<CategoryEntity> findCategoryEntityByKey(String key);
}
