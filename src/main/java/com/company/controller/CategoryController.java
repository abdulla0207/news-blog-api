package com.company.controller;

import com.company.dto.CategoryDTO;
import com.company.entity.CategoryEntity;
import com.company.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Category Controller is a Controller for Category entity manipulation.
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    // POST "/category/" request is send to API controller method
    // Request Method gets category object that should be added to database
    // It sends to service method and returns a response entity
    @PostMapping("/")
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryDTO){
        String response = categoryService.createCategory(categoryDTO);

        return ResponseEntity.ok(response);
    }

    // GET "/category/" request is send to API controller method
    // Request Method gets page and size that for pagination
    // It gets categories by pagination
    @GetMapping("/")
    public ResponseEntity<?> getCategoryList(@RequestParam("page") int page, @RequestParam("size") int size){
        Page<CategoryDTO> categoryEntities = categoryService.getCategoriesPagination(page, size);

        return ResponseEntity.ok(categoryEntities);
    }

    // PUT "/category/KEY_EXAMPLE" request is send to API controller method
    // It receives key of specific category and a new category object with updated fields
    @PutMapping("/{key}")
    public ResponseEntity<?> updateCategoryByKey(@RequestParam("key") String key, @RequestBody CategoryDTO categoryDTO){
        String response = categoryService.updateCategoryByKey(categoryDTO, key);

        return ResponseEntity.ok(response);
    }
}
