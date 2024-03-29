package com.company.controller;

import com.company.dto.RegionByLanguageDTO;
import com.company.dto.RegionDTO;
import com.company.enums.LanguageEnum;
import com.company.enums.ProfileRoleEnum;
import com.company.service.RegionService;
import com.company.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/regions")
@Tag(name = "Region API list")
public class RegionController {
    private final RegionService regionService;

    @Autowired
    public RegionController(RegionService regionService){
        this.regionService = regionService;
    }


    @Operation(summary = "Create region by ADMIN")
    @PostMapping("/")
    public ResponseEntity<RegionDTO> create(HttpServletRequest request, @RequestBody RegionDTO regionDTO){
        log.info("Create region {}", regionDTO);
        JwtUtil.checkForRole(request, ProfileRoleEnum.ADMIN);

        RegionDTO response = regionService.create(regionDTO);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update region by ADMIN")
    @PutMapping("/update/{id}")
    public ResponseEntity<RegionDTO> updateById(@PathVariable(name = "id") int id,
                                                HttpServletRequest request, @RequestBody RegionDTO regionDTO,
                                                @RequestHeader("Accept-Language") String lang){
        log.info("update region {}", regionDTO);
        JwtUtil.checkForRole(request, ProfileRoleEnum.ADMIN);

        RegionDTO response = regionService.updateById(id, regionDTO, lang);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete region by ADMIN")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable(name = "id") int id, HttpServletRequest request, @RequestHeader("Accept-Language") String lang){
        log.info("Remove region {}", id);
        JwtUtil.checkForRole(request, ProfileRoleEnum.ADMIN);

        String response = regionService.deleteById(id, lang);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get list of regions by ADMIN")
    @GetMapping("/")
    public ResponseEntity<List<RegionDTO>> getList(HttpServletRequest request){
        log.info("Get region list");
        JwtUtil.checkForRole(request, ProfileRoleEnum.ADMIN);

        List<RegionDTO> response = regionService.getList();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get list regions by language for any user")
    @GetMapping("/language")
    public ResponseEntity<List<RegionByLanguageDTO>> getListByLanguage(@RequestParam(name = "lang")String languageEnum){
        log.info("get region list by language");
        List<RegionByLanguageDTO> responseList = regionService.getListByLanguage(languageEnum);

        return ResponseEntity.ok(responseList);
    }
}
