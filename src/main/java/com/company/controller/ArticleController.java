package com.company.controller;

import com.company.dto.article.ArticleCreateDTO;
import com.company.dto.article.ArticleDTO;
import com.company.dto.article.ArticleFullDTO;
import com.company.dto.article.ArticleShortDTO;
import com.company.enums.ArticleStatusEnum;
import com.company.enums.ModeratorActionEnum;
import com.company.enums.ProfileRoleEnum;
import com.company.service.ArticleService;
import com.company.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Article Controller is a Controller for Article entity manipulation.
 */

@Slf4j
@RestController
@RequestMapping("/api/articles")
@Tag(name = "Article API list")
public class ArticleController {
    private ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService){
        this.articleService = articleService;
    }

    /** POST "/article/" request is sent to the API with the object
     * This method gets the object and sends it to service
     * It returns ok response with DTO object
     */
    @PostMapping("/writer")
    public ResponseEntity<?> createPost(@Valid @RequestBody ArticleCreateDTO articleDTO, HttpServletRequest request, @RequestHeader("Accept-Language") String lang){
        JwtUtil.checkForRole(request, ProfileRoleEnum.WRITER);
        Integer writerId = JwtUtil.getIdFromHeader(request);
        ArticleDTO res = articleService.createPost(articleDTO, writerId, lang);
        log.info("Article created {}", articleDTO);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/publisher/publish")
    public ResponseEntity<?> getArticlesForPublish(HttpServletRequest request, @RequestParam(name = "page") int page,
                                                              @RequestParam(name = "size") int size){
        log.info("Get articles for publish");
        JwtUtil.checkForRole(request, ProfileRoleEnum.PUBLISHER);

        Page<ArticleDTO> getArticlePaginationList = articleService.getArticlesForPublish(page, size);

        return ResponseEntity.ok(getArticlePaginationList);
    }

    @GetMapping("/moderator/review")
    public ResponseEntity<?> getArticleForReview(HttpServletRequest request, @RequestParam(name = "page") int page,
                                                 @RequestParam(name = "size") int size){
        log.info("Get articles for review");
        JwtUtil.checkForRole(request, ProfileRoleEnum.MODERATOR);

        Page<ArticleDTO> response = articleService.getArticleForReview(page, size);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/moderator/review/{articleId}")
    public ResponseEntity<?> updateModeratorAction(@PathVariable String articleId, HttpServletRequest request,
                                                   @RequestParam(name = "moderator_action") ModeratorActionEnum moderatorAction,
                                                   @RequestHeader("Accept-Language") String lang){
        log.info("Request for updating moderator_action of an article {}", moderatorAction);
        JwtUtil.checkForRole(request, ProfileRoleEnum.MODERATOR);
        Integer moderatorId = JwtUtil.getIdFromHeader(request);

        String response = articleService.updateModeratorAction(articleId, moderatorAction, moderatorId, lang);
        return ResponseEntity.ok(response);
    }

    /** GET "/article/" request is sent to the API with the page and size(size mainly the same number) from Parameters
     * This method sends the values to service and returns the list of articles
     */
    @GetMapping("/admin/")
    public ResponseEntity<?> getArticlePagination(@RequestHeader(name = "Accept-Language", defaultValue = "uz") String language,
                                                  @RequestParam("page") int page, @RequestParam("size") int size, HttpServletRequest request){
        log.info("Get articles pagination for admin");
        JwtUtil.checkForRole(request, ProfileRoleEnum.ADMIN);
        Page<ArticleDTO> getArticlePaginationList = articleService.getArticlePagination(language, page, size);

        return ResponseEntity.ok(getArticlePaginationList);
    }

    /** DELETE "/article/UUID" request is send with the uuid of the object to be deleted
     * It gets the uuid, calls the service method by sending uuid and gets the result of String
     * Method returns Ok result.
     */
    @DeleteMapping("/moderator/{uuid}")
    public ResponseEntity<?> deleteById(@PathVariable String uuid, HttpServletRequest request, @RequestHeader("Accept-Language") String lang){
        log.info("Request for deleting article {}", uuid);
        JwtUtil.checkForRole(request, ProfileRoleEnum.MODERATOR);
        String response = articleService.deleteById(uuid, lang);

        return ResponseEntity.ok(response);
    }

    /** PUT "/article/UUID" request is send with uuid, and new object with new values of fields
     * Calls the service method to update by sending UUID and OBJECT
     * Method returns Ok result
     *
     */
    @PutMapping("/writer/{uuid}")
    public ResponseEntity<?> updateById(@PathVariable String uuid, @RequestBody ArticleCreateDTO articleDTO,
                                        HttpServletRequest request, @RequestHeader("Accept-Language") String lang){
        log.info("Request for updating an article {}", uuid);
        Integer currentUserId = JwtUtil.getIdFromHeader(request);
        String response = articleService.updateById(uuid, articleDTO, currentUserId, lang);

        return ResponseEntity.ok(response);
    }

    /** GET "/article/UUID" request is send with uuid
     * Calls service method and GETS particular object with uuid
     * It returns ok result with object
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<?> getById(@PathVariable String uuid, @RequestHeader("Accept-Language") String lang){
        log.info("Get Article by id");
        ArticleDTO articleDTO = articleService.getById(uuid, lang);

        return ResponseEntity.ok(articleDTO);
    }

    /** GET "/article/order/published-date/" request is send to API with page and size
     * Gets all articles, filters articles by published date and applies pagination,
     */
    @GetMapping("/order/published-date/")
    public ResponseEntity<?> getArticlesOrderedByPublishedDate(@RequestHeader(name = "Accept-Language", defaultValue = "uz") String language,
                                                               @RequestParam("page") int page, @RequestParam("size") int size){
        log.info("Get Articles order by published date");
        Page<ArticleDTO> response = articleService.findAllArticleByPublishedDate(page, size, language);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/user/likes")
    public ResponseEntity<?> getLikedArticlesForUser(HttpServletRequest request){
        log.info("get liked articles for user");
        Integer idFromHeader = JwtUtil.getIdFromHeader(request);
        List<ArticleShortDTO> response = articleService.getLikedArticlesForUser(idFromHeader);

        return ResponseEntity.ok(response);
    }

    /** GET "/article/order/title" request is send to API with page and size
     * Gets all articles, filters articles by TITLE and applies pagination
     */
    @GetMapping("/order/title/")
    public ResponseEntity<?> getArticlesOrderedByTitle(@RequestParam("page") int page, @RequestParam("size") int size,
                                                         @RequestHeader(name = "Accept-Language", defaultValue = "uz") String language){
        log.info("Get Article order by title");
        Page<ArticleDTO> response = articleService.findArticlesOrderedByTitle(page, size, language);

        return ResponseEntity.ok(response);
    }

    /** GET "/article/search?title=TITLE_FROM_SEARCH" request is send to API
     * This method gets the title from the URI query parameter and sends
     *the value to service to GET article by title from DB.
     */
    @GetMapping("/search/")
    public ResponseEntity<?> searchArticlesByTitle(@RequestParam("title") String title,
                                                   @RequestHeader(name = "Accept-Language", defaultValue = "uz") String language){
        log.info("search articles by title");
        List<ArticleDTO> articleDTOS = articleService.searchArticlesByTitle(title, language);

        return ResponseEntity.ok(articleDTOS);
    }

    /** GET "/article/category?key=KEY_EXAMPLE" request is send to API
     * This method gets articles by category's key and applies pagination
     */
    @GetMapping("/category/{key}")
    public ResponseEntity<?> getArticlesByCategory(@RequestHeader(name = "Accept-Language", defaultValue = "uz") String language, @PathVariable String key,
                                                   @RequestParam("page") int page,
                                                   @RequestParam("size") int size){
        log.info("Get Articles by category");
        Page<ArticleDTO> articleDTOS = articleService.getArticlesByCategory(key, page, size, language);

        return ResponseEntity.ok(articleDTOS);
    }

    @PutMapping("/publisher/status/{uuid}")
    public ResponseEntity<String> changeStatus(@PathVariable String uuid, HttpServletRequest request,
                                               @RequestParam(name = "article_status")ArticleStatusEnum articleStatusEnum,
                                               @RequestHeader("Accept-Language") String lang){
        log.info("Change article status");
        JwtUtil.checkForRole(request, ProfileRoleEnum.PUBLISHER);
        Integer publisherId = JwtUtil.getIdFromHeader(request);
        String response = articleService.changeStatus(uuid, publisherId, articleStatusEnum, lang);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/last-five/by-type/{typeId}")
    public ResponseEntity<?> getLastFiveByType(@PathVariable int typeId, @RequestHeader(name = "Accept-Language", defaultValue = "uz") String language){
        log.info("get last five articles");
        List<ArticleShortDTO> response = articleService.getLastFiveByType(typeId, language);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/last-eight")
    public ResponseEntity<?> getLastEightNotIncludeId(@RequestParam(name = "uuid") List<String> uuid,
                                                      @RequestHeader(name = "Accept-Language", defaultValue = "uz") String language){
        log.info("get last eight articles not included ids");
        List<ArticleShortDTO> response = articleService.getLastEightNotIncludeId(uuid, language);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/language/{uuid}")
    public ResponseEntity<?> getArticleByIdAndLanguage(@PathVariable(name = "uuid") String uuid, @RequestHeader(name = "Accept-Language", defaultValue = "uz") String language){
        log.info("get articles by id and language");
        ArticleFullDTO response = articleService.getArticlesByIdAndLanguage(uuid, language);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/viewed/weekly")
    public ResponseEntity<?> getMostViewedArticlesInAWeek(@RequestHeader(name = "Accept-Language", defaultValue = "uz") String language,
                                                          @RequestParam("page") int page, @RequestParam("size") int size){
        log.info("get most viewed articles");
        Page<ArticleShortDTO> response = articleService.getMostViewedArticleInAWeek(language, page, size);

        return ResponseEntity.ok(response);
    }
}
