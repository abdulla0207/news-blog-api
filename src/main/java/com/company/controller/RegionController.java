package com.company.controller;

import com.company.dto.RegionByLanguageDTO;
import com.company.dto.RegionDTO;
import com.company.enums.LanguageEnum;
import com.company.enums.ProfileRoleEnum;
import com.company.service.RegionService;
import com.company.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/regions")
public class RegionController {
    private final RegionService regionService;

    @Autowired
    public RegionController(RegionService regionService){
        this.regionService = regionService;
    }

    @PostMapping("/")
    public ResponseEntity<RegionDTO> create(HttpServletRequest request, @RequestBody RegionDTO regionDTO){
        log.info("Create region {}", regionDTO);
        JwtUtil.checkForRole(request, ProfileRoleEnum.ADMIN);

        RegionDTO response = regionService.create(regionDTO);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RegionDTO> updateById(@PathVariable(name = "id") int id,
                                                HttpServletRequest request, @RequestBody RegionDTO regionDTO,
                                                @RequestHeader("Accept-Language") String lang){
        log.info("update region {}", regionDTO);
        JwtUtil.checkForRole(request, ProfileRoleEnum.ADMIN);

        RegionDTO response = regionService.updateById(id, regionDTO, lang);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable(name = "id") int id, HttpServletRequest request){
        log.info("Remove region {}", id);
        JwtUtil.checkForRole(request, ProfileRoleEnum.ADMIN);

        String response = regionService.deleteById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<List<RegionDTO>> getList(HttpServletRequest request){
        log.info("Get region list");
        JwtUtil.checkForRole(request, ProfileRoleEnum.ADMIN);

        List<RegionDTO> response = regionService.getList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/language")
    public ResponseEntity<List<RegionByLanguageDTO>> getListByLanguage(@RequestParam(name = "lang")String languageEnum){
        log.info("get region list by language");
        List<RegionByLanguageDTO> responseList = regionService.getListByLanguage(languageEnum);

        return ResponseEntity.ok(responseList);
    }
}
