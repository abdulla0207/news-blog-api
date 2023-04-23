package com.company.service;

import com.company.dto.CategoryDTO;
import com.company.entity.CategoryEntity;
import com.company.exception.CategoryCreateException;
import com.company.exception.ItemNotFoundException;
import com.company.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
        if(categoryDTO.getNameUz().isEmpty() || categoryDTO.getNameUz().isBlank())
            throw new CategoryCreateException("Name of the category in Uzbek should be filled");
        if(categoryDTO.getNameEn().isEmpty() || categoryDTO.getNameEn().isBlank())
            throw new CategoryCreateException("Name of the category in English should be filled");
        String[] s = categoryDTO.getNameEn().toLowerCase().split(" ");

        String newSlag = String.join("_", s);


        CategoryEntity entity = toEntity(categoryDTO);
        entity.setSlag(newSlag);

        categoryRepository.save(entity);
        return "Category created";
    }

    public String updateCategoryByKey(CategoryDTO categoryDTO, String key){
        String[] s = categoryDTO.getNameEn().toLowerCase().split(" ");

        String newSlag = String.join("_", s);

        int i = categoryRepository.updateCategory(categoryDTO.getNameUz(), categoryDTO.getNameEn(),
                newSlag, categoryDTO.isVisible(), key);

        if(i <= 0)
            throw new ItemNotFoundException("Category not found with this keyword");

        return "Updated";
    }


    private CategoryEntity toEntity(CategoryDTO categoryDTO){
        CategoryEntity entity = new CategoryEntity();
        entity.setNameUz(categoryDTO.getNameUz());
        entity.setNameEn(categoryDTO.getNameEn());
        entity.setCreatedAt(LocalDateTime.now());

        return entity;
    }

    public Page<CategoryDTO> getCategoriesPagination(int page, int size) {
        PageRequest of = PageRequest.of(page, size);

        Page<CategoryEntity> categoryEntityPage = categoryRepository.findCategoriesPagination(of);

        long totalElements = categoryEntityPage.getTotalElements();
        List<CategoryDTO> categoryDTOS = categoryEntityPage.get().map(this::toDto).toList();

        Page<CategoryDTO> res = new PageImpl<>(categoryDTOS, of, totalElements);

        return res;
    }


    private CategoryDTO toDto(CategoryEntity categoryEntity){
        CategoryDTO dto = new CategoryDTO();

        dto.setNameUz(categoryEntity.getNameUz());
        dto.setNameEn(categoryEntity.getNameEn());
        dto.setVisible(categoryEntity.isVisible());
        dto.setKey(categoryEntity.getKey());
        dto.setSlag(categoryEntity.getSlag());
        dto.setCreatedAt(categoryEntity.getCreatedAt());

        return dto;
    }
}
