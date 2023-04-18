package com.company.controller;

import com.company.dto.CategoryDTO;
import com.company.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
