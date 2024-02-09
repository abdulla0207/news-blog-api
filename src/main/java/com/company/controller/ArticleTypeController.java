package com.company.controller;

import com.company.dto.ArticleTypeCreateDTO;
import com.company.dto.ArticleTypeDTO;
import com.company.dto.ArticleTypeByLanguageDTO;
import com.company.enums.LanguageEnum;
import com.company.enums.ProfileRoleEnum;
import com.company.service.ArticleTypeService;
import com.company.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/article/types")
@Tag(name = "Article Type API list")
public class ArticleTypeController {

    private final ArticleTypeService articleTypeService;

    @Autowired
    public ArticleTypeController(ArticleTypeService articleTypeService){
        this.articleTypeService = articleTypeService;
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@Valid @RequestBody ArticleTypeCreateDTO articleTypeDTO, HttpServletRequest request){
        log.info("create article type {}", articleTypeDTO);
        JwtUtil.checkForRole(request, ProfileRoleEnum.ADMIN);

        ArticleTypeDTO responseDTO = articleTypeService.create(articleTypeDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateById(@PathVariable int id, @RequestBody ArticleTypeDTO dto, HttpServletRequest request,
                                        @RequestHeader("Accept-Language") String lang){
        log.info("update article type by id {}", dto);
        JwtUtil.checkForRole(request, ProfileRoleEnum.ADMIN);
        ArticleTypeDTO responseDTO = articleTypeService.updateById(id, dto, lang);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/")
    public ResponseEntity<?> getList(HttpServletRequest request, @RequestParam(name = "page") int page,
                                     @RequestParam(name = "size") int size){
        log.info("Get list of Article Types");
        JwtUtil.checkForRole(request, ProfileRoleEnum.ADMIN);
        Page<ArticleTypeDTO> articleDTOS = articleTypeService.getList(page, size);

        return ResponseEntity.ok(articleDTOS);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable int id, HttpServletRequest request, @RequestHeader("Accept-Language") String lang){
        log.info("delete article type {}", id);
        JwtUtil.checkForRole(request,ProfileRoleEnum.ADMIN);

        String response = articleTypeService.deleteById(id, lang);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/language")
    public ResponseEntity<?> getListByLanguage(@RequestParam(name = "lang", defaultValue = "ENGLISH") LanguageEnum lang){
        log.info("get list of article types by language");
        List<ArticleTypeByLanguageDTO> dtos = articleTypeService.getListByLanguage(lang);

        return ResponseEntity.ok(dtos);
    }
}
