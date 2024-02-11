package com.company.controller;

import com.company.dto.ProfileDTO;
import com.company.enums.ProfileRoleEnum;
import com.company.service.ProfileService;
import com.company.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Profile Controller is mainly for ADMINS
 * Here admins can view the list of users, edit any users, delete them and etc.
 * This will be only accessible in ADMIN page FOR ADMINS
 */

@Slf4j
@RestController
@RequestMapping("/api/profiles")
@Tag(name = "Profile API list")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService){
        this.profileService = profileService;
    }

    // POST "/profile/" request is send to API from front. It receives headerToken that was generated from JWT when user is logged in
    //and the body object that should be created
    // This method decodes the headerToken to JwtDTO object and sends to create service
    @Operation(summary = "Create user profile by ADMIN")
    @PostMapping("/")
    public ResponseEntity<?> create(HttpServletRequest request, @RequestBody ProfileDTO profileDTO, @RequestHeader("Accept-Language") String lang){
        log.info("create user {}", profileDTO);
        JwtUtil.checkForRole(request, ProfileRoleEnum.ADMIN);
        int id = JwtUtil.getIdFromHeader(request);
        ProfileDTO responseDTO = profileService.create(profileDTO, id, lang);

        return ResponseEntity.ok(responseDTO);
    }

    // DELETE "/profile/1" request is send to API controller method
    // id is received from URI and headerToken from the field of Authorization
    // Method decodes the token and calls deleteById method that deletes profile by id
    @Operation(summary = "Remove user profile by ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id, HttpServletRequest request, @RequestHeader("Accept-Language") String lang){
        log.info("remove user {}", id);
        JwtUtil.checkForRole(request, ProfileRoleEnum.ADMIN);
        String s = profileService.deleteById(id, lang);

        return ResponseEntity.ok(s);
    }

    // GET "/profile/" request is send to API controller method from front
    // It receives headerToken that will identify if this request is send by ADMIN
    // page and size from URI query
    // Method decodes headerToken and gets list of profiles from service
    @Operation(summary = "Get user profile list for ADMIN")
    @GetMapping("/")
    public ResponseEntity<Page<ProfileDTO>> getList(HttpServletRequest request,
                                     @RequestParam("page") int page, @RequestParam("size") int size){
        log.info("get user list");
        JwtUtil.checkForRole(request, ProfileRoleEnum.ADMIN);

        Page<ProfileDTO> profileList = profileService.getProfileList(page, size);

        return ResponseEntity.ok(profileList);
    }

    // PUT "profile/admin/1" request is send to API controller method from front
    // Method gets profile object, id and headerToken
    // Decodes headerToken, and sends all properties to service method to Update specific profile
    @Operation(summary = "Update user profile by ADMIN")
    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateByAdmin(@RequestBody ProfileDTO profileDTO, @PathVariable int id, HttpServletRequest request,
                                           @RequestHeader("Accept-Language") String lang){
        log.info("update user for admin {}", id);
        JwtUtil.checkForRole(request, ProfileRoleEnum.ADMIN);

        String response = profileService.update(profileDTO, id, lang);

        return ResponseEntity.ok(response);
    }

    // PUT "/profile/edit" request is send to API
    // Method gets the update version of object and headerToken
    // It decodes the token and calls updateByProfile method from service
    @Operation(summary = "Update user profile for user")
    @PutMapping("/edit")
    public ResponseEntity<?> updateByProfile(@RequestBody ProfileDTO profileDTO, HttpServletRequest request,
                                             @RequestHeader("Accept-Language") String lang){
        log.info("update user for user {}", profileDTO);
        int tokenId = JwtUtil.getIdFromHeader(request);

        String response  = profileService.updateByProfile(profileDTO, tokenId, lang);

        return ResponseEntity.ok(response);
    }
}
