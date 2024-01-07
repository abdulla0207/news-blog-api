package com.company.controller;

import com.company.dto.JwtDTO;
import com.company.dto.ProfileDTO;
import com.company.enums.ProfileRoleEnum;
import com.company.service.ProfileService;
import com.company.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Profile Controller is mainly for ADMINS
 * Here admins can view the list of users, edit any users, delete them and etc.
 * This will be only accessible in ADMIN page FOR ADMINS
 */

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService){
        this.profileService = profileService;
    }

    // POST "/profile/" request is send to API from front. It receives headerToken that was generated from JWT when user is logged in
    //and the body object that should be created
    // This method decodes the headerToken to JwtDTO object and sends to create service
    @PostMapping("/")
    public ResponseEntity<?> create(HttpServletRequest request, @RequestBody ProfileDTO profileDTO){

        JwtUtil.checkForAdmin(request, ProfileRoleEnum.ADMIN);
        int id = JwtUtil.getIdFromHeader(request);
        ProfileDTO responseDTO = profileService.create(profileDTO, id);

        return ResponseEntity.ok(responseDTO);
    }

    // DELETE "/profile/1" request is send to API controller method
    // id is received from URI and headerToken from the field of Authorization
    // Method decodes the token and calls deleteById method that deletes profile by id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id, HttpServletRequest request){
        JwtUtil.checkForAdmin(request, ProfileRoleEnum.ADMIN);
        String s = profileService.deleteById(id);

        return ResponseEntity.ok(s);
    }

    // GET "/profile/" request is send to API controller method from front
    // It receives headerToken that will identify if this request is send by ADMIN
    // page and size from URI query
    // Method decodes headerToken and gets list of profiles from service
    @GetMapping("/")
    public ResponseEntity<?> getList(HttpServletRequest request,
                                     @RequestParam("page") int page, @RequestParam("size") int size){
        JwtUtil.checkForAdmin(request, ProfileRoleEnum.ADMIN);

        Page<ProfileDTO> profileList = profileService.getProfileList(page, size);

        return ResponseEntity.ok(profileList);
    }

    // PUT "profile/admin/1" request is send to API controller method from front
    // Method gets profile object, id and headerToken
    // Decodes headerToken, and sends all properties to service method to Update specific profile
    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateByAdmin(@RequestBody ProfileDTO profileDTO, @PathVariable int id, HttpServletRequest request){
        JwtUtil.checkForAdmin(request, ProfileRoleEnum.ADMIN);

        String response = profileService.update(profileDTO, id);

        return ResponseEntity.ok(response);
    }

    // PUT "/profile/edit" request is send to API
    // Method gets the update version of object and headerToken
    // It decodes the token and calls updateByProfile method from service
    @PutMapping("/edit")
    public ResponseEntity<?> updateByProfile(@RequestBody ProfileDTO profileDTO, HttpServletRequest request){
        int tokenId = JwtUtil.getIdFromHeader(request);

        String response  = profileService.updateByProfile(profileDTO, tokenId);

        return ResponseEntity.ok(response);
    }
}
