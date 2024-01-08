package com.company.controller;

import com.company.dto.ArticleDTO;
import com.company.dto.ArticleTypeDTO;
import com.company.dto.ArticleTypeResponseDTO;
import com.company.enums.LanguageEnum;
import com.company.enums.ProfileRoleEnum;
import com.company.service.ArticleTypeService;
import com.company.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article/type")
public class ArticleTypeController {

    private final ArticleTypeService articleTypeService;

    @Autowired
    public ArticleTypeController(ArticleTypeService articleTypeService){
        this.articleTypeService = articleTypeService;
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody ArticleTypeDTO articleTypeDTO, HttpServletRequest request){
        JwtUtil.checkForAdmin(request, ProfileRoleEnum.ADMIN);

        ArticleTypeDTO responseDTO = articleTypeService.create(articleTypeDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateById(@PathVariable int id, @RequestBody ArticleTypeDTO dto, HttpServletRequest request){
        JwtUtil.checkForAdmin(request, ProfileRoleEnum.ADMIN);
        ArticleTypeDTO responseDTO = articleTypeService.updateById(id, dto);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/")
    public ResponseEntity<?> getList(HttpServletRequest request, @RequestParam(name = "page") int page,
                                     @RequestParam(name = "size") int size){
        JwtUtil.checkForAdmin(request, ProfileRoleEnum.ADMIN);
        Page<ArticleTypeDTO> articleDTOS = articleTypeService.getList(page, size);

        return ResponseEntity.ok(articleDTOS);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable int id, HttpServletRequest request){
        JwtUtil.checkForAdmin(request,ProfileRoleEnum.ADMIN);

        String response = articleTypeService.deleteById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/language")
    public ResponseEntity<?> getListByLanguage(@RequestParam(name = "lang", defaultValue = "ENGLISH") LanguageEnum lang){
        List<ArticleTypeResponseDTO> dtos = articleTypeService.getListByLanguage(lang);

        return ResponseEntity.ok(dtos);
    }
}
