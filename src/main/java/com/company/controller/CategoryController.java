package com.company.controller;

import com.company.dto.CategoryDTO;
import com.company.entity.CategoryEntity;
import com.company.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryDTO){
        String response = categoryService.createCategory(categoryDTO);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<?> getCategoryList(@RequestParam("page") int page, @RequestParam("size") int size){
        Page<CategoryDTO> categoryEntities = categoryService.getCategoriesPagination(page, size);

        return ResponseEntity.ok(categoryEntities);
    }

    @PutMapping("/{key}")
    public ResponseEntity<?> updateCategoryByKey(@RequestParam("key") String key, @RequestBody CategoryDTO categoryDTO){
        String response = categoryService.updateCategoryByKey(categoryDTO, key);

        return ResponseEntity.ok(response);
    }
}
