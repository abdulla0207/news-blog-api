package com.company.service;

import com.company.dto.CategoryByLanguageDTO;
import com.company.dto.CategoryCreateDTO;
import com.company.dto.CategoryDTO;
import com.company.entity.CategoryEntity;
import com.company.entity.RegionEntity;
import com.company.enums.LanguageEnum;
import com.company.exception.CategoryCreateException;
import com.company.exception.ItemNotFoundException;
import com.company.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        System.out.println("In constr" + getClass().getSimpleName());
        this.categoryRepository = categoryRepository;
    }

    public CategoryEntity getCategoryByKey(String key) {
        Optional<CategoryEntity> categoryEntity = categoryRepository.findCategoryEntityByKey(key);

        if(categoryEntity.isEmpty()) {
            log.warn("Category not found {}", key);
            throw new ItemNotFoundException("Category not found");
        }

        return categoryEntity.get();
    }

    public String createCategory(CategoryCreateDTO categoryDTO) {
        String[] s = categoryDTO.nameEn().toLowerCase().split(" ");

        String newSlag = String.join("_", s);


        CategoryEntity entity = toEntity(categoryDTO);
        entity.setSlag(newSlag);
        categoryRepository.save(entity);
        return "Category created";
    }

    public String updateCategoryByKey(CategoryDTO categoryDTO, int id){
        String[] s = categoryDTO.nameEn().toLowerCase().split(" ");

        String newSlag = String.join("_", s);

        int i = categoryRepository.updateCategory(categoryDTO.nameUz(), categoryDTO.nameEn(),
                newSlag, categoryDTO.visible(), id);

        if(i <= 0) {
            log.warn("Category not found with key {}", newSlag);
            throw new ItemNotFoundException("Category not found with this keyword");
        }

        return "Updated";
    }


    private CategoryEntity toEntity(CategoryCreateDTO categoryDTO){
        CategoryEntity entity = new CategoryEntity();
        entity.setNameUz(categoryDTO.nameUz());
        entity.setNameEn(categoryDTO.nameEn());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setVisible(categoryDTO.visible());
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
        CategoryDTO dto = new CategoryDTO(
                categoryEntity.getId(),
                categoryEntity.getNameUz(),
                categoryEntity.getNameEn(),
                categoryEntity.isVisible(),
                categoryEntity.getCreatedAt(),
                categoryEntity.getKey(),
                categoryEntity.getSlag()
        );
        return dto;
    }

    public String deleteById(int id) {
        Optional<CategoryEntity> byId = categoryRepository.findById(id);
        if(byId.isEmpty()) {
            log.warn("Category not found with id {}", id);
            throw new ItemNotFoundException("Category with this id not found");
        }

        categoryRepository.deleteById(id);
        return "Category has been deleted";
    }

    public List<CategoryByLanguageDTO> getByLanguage(String languageEnum) {
        List<CategoryEntity> allWhereVisibleIsTrue = categoryRepository.findAllWhereVisibleIsTrue();

        List<CategoryByLanguageDTO> response = new ArrayList<>();
        allWhereVisibleIsTrue.forEach(categoryEntity -> {
            String name = null;
            switch (languageEnum){
                case "uz" -> name = categoryEntity.getNameUz();
                case "en" -> name = categoryEntity.getNameEn();
                default -> {
                    log.warn("Wrong language");
                    throw new IllegalArgumentException("Wrong language");
                }
            }
            response.add(new CategoryByLanguageDTO(categoryEntity.getId(), name,
                    categoryEntity.isVisible(), categoryEntity.getKey(), categoryEntity.getSlag()));
        });

        return response;
    }
}
