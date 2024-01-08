package com.company.controller;

import com.company.dto.ArticleDTO;
import com.company.service.ArticleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Article Controller is a Controller for Article entity manipulation.
 */

@RestController
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService){
        this.articleService = articleService;
    }

    /** POST "/article/" request is sent to the API with the object
     * This method gets the object and sends it to service
     * It returns ok response with DTO object
     */
    @PostMapping("/")
    public ResponseEntity<?> createPost(@RequestBody ArticleDTO articleDTO, HttpServletRequest request){

        ArticleDTO res = articleService.createPost(articleDTO);
        return ResponseEntity.ok(res);
    }

    /** GET "/article/" request is sent to the API with the page and size(size mainly the same number) from Parameters
     * This method sends the values to service and returns the list of articles
     */
    @GetMapping("/")
    public ResponseEntity<?> getArticlePagination(@RequestParam("page") int page, @RequestParam("size") int size){
        Page<ArticleDTO> getArticlePaginationList = articleService.getArticlePagination(page, size);

        return ResponseEntity.ok(getArticlePaginationList);
    }

    /** DELETE "/article/UUID" request is send with the uuid of the object to be deleted
     * It gets the uuid, calls the service method by sending uuid and gets the result of String
     * Method returns Ok result.
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteById(@PathVariable String uuid){
        String response = articleService.deleteById(uuid);

        return ResponseEntity.ok(response);
    }

    /** PUT "/article/UUID" request is send with uuid, and new object with new values of fields
     * Calls the service method to update by sending UUID and OBJECT
     * Method returns Ok result
     */
    @PutMapping("/{uuid}")
    public ResponseEntity<?> updateById(@PathVariable String uuid, @RequestBody ArticleDTO articleDTO){
        String response = articleService.updateById(uuid, articleDTO);

        return ResponseEntity.ok(response);
    }

    /** GET "/article/UUID" request is send with uuid
     * Calls service method and GETS particular object with uuid
     * It returns ok result with object
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<?> getById(@PathVariable String uuid){
        ArticleDTO articleDTO = articleService.getById(uuid);

        return ResponseEntity.ok(articleDTO);
    }

    /** GET "/article/order/published-date/" request is send to API with page and size
     * Gets all articles, filters articles by published date and applies pagination,
     */
    @GetMapping("/order/published-date/")
    public ResponseEntity<?> getArticlesOrderedByPublishedDate(@RequestParam("page") int page, @RequestParam("size") int size){
        Page<ArticleDTO> response = articleService.findAllArticleByPublishedDate(page, size);
        return ResponseEntity.ok(response);
    }

    /** GET "/article/order/title" request is send to API with page and size
     * Gets all articles, filters articles by TITLE and applies pagination
     */
    @GetMapping("/order/title")
    public ResponseEntity<?> getArticlesByOrderedByTitle(@RequestParam("page") int page, @RequestParam("size") int size){
        Page<ArticleDTO> response = articleService.findArticlesOrderedByTitleUz(page, size);

        return ResponseEntity.ok(response);
    }

    /** GET "/article/search?title=TITLE_FROM_SEARCH" request is send to API
     * This method gets the title from the URI query parameter and sends
     *the value to service to GET article by title from DB.
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchArticlesByTitleUz(@RequestParam("title") String title){
        List<ArticleDTO> articleDTOS = articleService.searchArticlesByTitleUz(title);

        return ResponseEntity.ok(articleDTOS);
    }

    /** GET "/article/category?key=KEY_EXAMPLE" request is send to API
     * This method gets articles by category's key and applies pagination
     */
    @GetMapping("/category/{key}")
    public ResponseEntity<?> getArticlesByCategory(@PathVariable String key,
                                                   @RequestParam("page") int page,
                                                   @RequestParam("size") int size){
        Page<ArticleDTO> articleDTOS = articleService.getArticlesByCategory(key, page, size);

        return ResponseEntity.ok(articleDTOS);
    }
}
