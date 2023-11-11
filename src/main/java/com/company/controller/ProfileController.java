package com.company.controller;

import com.company.dto.JwtDTO;
import com.company.dto.ProfileDTO;
import com.company.service.ProfileService;
import com.company.util.JwtUtil;
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
    public ResponseEntity<?> create(@RequestHeader("Authorization") String headerToken, @RequestBody ProfileDTO profileDTO){

        JwtDTO jwtDTO = JwtUtil.decode(JwtUtil.getToken(headerToken));
        
        ProfileDTO responseDTO = profileService.create(profileDTO, jwtDTO);

        return ResponseEntity.ok(responseDTO);
    }

    // DELETE "/profile/1" request is send to API controller method
    // id is received from URI and headerToken from the field of Authorization
    // Method decodes the token and calls deleteById method that deletes profile by id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id, @RequestHeader("Authorization") String headerToken){
        JwtDTO jwtDTO = JwtUtil.decode(JwtUtil.getToken(headerToken));
        String s = profileService.deleteById(id, jwtDTO);

        return ResponseEntity.ok(s);
    }

    // GET "/profile/" request is send to API controller method from front
    // It receives headerToken that will identify if this request is send by ADMIN
    // page and size from URI query
    // Method decodes headerToken and gets list of profiles from service
    @GetMapping("/")
    public ResponseEntity<?> getList(@RequestHeader("Authorization") String headerToken,
                                     @RequestParam("page") int page, @RequestParam("size") int size){
        JwtDTO jwtDTO = JwtUtil.decode(JwtUtil.getToken(headerToken));

        Page<ProfileDTO> profileList = profileService.getProfileList(page, size, jwtDTO);

        return ResponseEntity.ok(profileList);
    }

    // PUT "profile/admin/1" request is send to API controller method from front
    // Method gets profile object, id and headerToken
    // Decodes headerToken, and sends all properties to service method to Update specific profile
    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateByAdmin(@RequestBody ProfileDTO profileDTO, @PathVariable int id, @RequestHeader("Authorization") String headerToken){
        JwtDTO jwtDTO = JwtUtil.decode(JwtUtil.getToken(headerToken));

        String response = profileService.update(profileDTO, id, jwtDTO);

        return ResponseEntity.ok(response);
    }

    // PUT "/profile/edit" request is send to API
    // Method gets the update version of object and headerToken
    // It decodes the token and calls updateByProfile method from service
    @PutMapping("/edit")
    public ResponseEntity<?> updateByProfile(@RequestBody ProfileDTO profileDTO, @RequestHeader("Authorization") String headerToken){
        JwtDTO jwtDTO = JwtUtil.decode(JwtUtil.getToken(headerToken));

        String response  = profileService.updateByProfile(profileDTO, jwtDTO);

        return ResponseEntity.ok(response);
    }
}
