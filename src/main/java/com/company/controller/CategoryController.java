package com.company.controller;

import com.company.dto.CategoryByLanguageDTO;
import com.company.dto.CategoryDTO;
import com.company.enums.LanguageEnum;
import com.company.enums.ProfileRoleEnum;
import com.company.service.CategoryService;
import com.company.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Category Controller is a Controller for Category entity manipulation.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    /** POST "/category/" request is send to API controller method
     * Request Method gets category object that should be added to database
     * It sends to service method and returns a response entity
     **/
    @PostMapping("/")
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryDTO, HttpServletRequest request){
        JwtUtil.checkForRole(request, ProfileRoleEnum.ADMIN);
        String response = categoryService.createCategory(categoryDTO);

        return ResponseEntity.ok(response);
    }

    /** GET "/category/" request is send to API controller method
     * Request Method gets page and size that for pagination
     * It gets categories by pagination
     */
    @GetMapping("/")
    public ResponseEntity<?> getCategoryList(@RequestParam("page") int page, @RequestParam("size") int size, HttpServletRequest request){
        JwtUtil.checkForRole(request, ProfileRoleEnum.ADMIN);
        Page<CategoryDTO> categoryEntities = categoryService.getCategoriesPagination(page, size);

        return ResponseEntity.ok(categoryEntities);
    }

    /** PUT "/category/KEY_EXAMPLE" request is send to API controller method
     * It receives key of specific category and a new category object with updated fields
     */
    @PutMapping("/{key}")
    public ResponseEntity<?> updateCategoryById(@RequestParam("id") int id, @RequestBody CategoryDTO categoryDTO, HttpServletRequest request){
        JwtUtil.checkForRole(request, ProfileRoleEnum.ADMIN);
        String response = categoryService.updateCategoryByKey(categoryDTO, id);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable(name = "id") int id, HttpServletRequest request){
        JwtUtil.checkForRole(request, ProfileRoleEnum.ADMIN);

        String response = categoryService.deleteById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/language")
    public ResponseEntity<?> getByLanguage(@RequestParam LanguageEnum languageEnum){
        List<CategoryByLanguageDTO> response = categoryService.getByLanguage(languageEnum);

        return ResponseEntity.ok(response);
    }
}
