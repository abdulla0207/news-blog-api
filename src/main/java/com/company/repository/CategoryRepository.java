package com.company.repository;

import com.company.entity.CategoryEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {

    Optional<CategoryEntity> findCategoryEntityByKey(String key);

    @Modifying
    @Transactional
    @Query("update CategoryEntity set nameUz=?1, nameEn = ?2, slag = ?3, visible = ?4 where key = ?5")
    int updateCategory(String nameUz, String nameEn, String slag, boolean visible, String key);

    @Query("select c from CategoryEntity as c")
    Page<CategoryEntity> findCategoriesPagination(PageRequest of);
}
