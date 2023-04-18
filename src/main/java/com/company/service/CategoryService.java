package com.company.service;

import com.company.dto.CategoryDTO;
import com.company.entity.CategoryEntity;
import com.company.exception.CategoryCreateException;
import com.company.exception.ItemNotFoundException;
import com.company.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    public CategoryEntity getCategoryByKey(String key) {
        Optional<CategoryEntity> categoryEntity = categoryRepository.findCategoryEntityByKey(key);

        if(categoryEntity.isEmpty())
            throw new ItemNotFoundException("Category not found");

        return categoryEntity.get();
    }

    public String createCategory(CategoryDTO categoryDTO) {
        if(categoryDTO.getNameRu().isEmpty() || categoryDTO.getNameRu().isBlank())
            throw new CategoryCreateException("Name of the category in Russian should be filled");
        if(categoryDTO.getNameEng().isEmpty() || categoryDTO.getNameEng().isBlank())
            throw new CategoryCreateException("Name of the category in English should be filled");

        CategoryEntity entity = toEntity(categoryDTO);

        categoryRepository.save(entity);
        return "Category created";
    }


    private CategoryEntity toEntity(CategoryDTO categoryDTO){
        CategoryEntity entity = new CategoryEntity();
        entity.setNameRu(categoryDTO.getNameRu());
        entity.setNameEng(categoryDTO.getNameEng());
        entity.setCreatedAt(LocalDateTime.now());

        return entity;
    }
}
