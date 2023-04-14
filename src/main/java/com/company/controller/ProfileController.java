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
 * This will be only accessible in ADMIN page
 */

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService){
        this.profileService = profileService;
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestHeader("Authorization") String headerToken, @RequestBody ProfileDTO profileDTO){

        JwtDTO jwtDTO = JwtUtil.decode(JwtUtil.getToken(headerToken));
        
        ProfileDTO responseDTO = profileService.create(profileDTO, jwtDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id, @RequestHeader("Authorization") String headerToken){
        JwtDTO jwtDTO = JwtUtil.decode(JwtUtil.getToken(headerToken));
        String s = profileService.deleteById(id, jwtDTO);

        return ResponseEntity.ok(s);
    }

    @GetMapping("/")
    public ResponseEntity<?> getList(@RequestHeader("Authorization") String headerToken,
                                     @RequestParam("page") int page, @RequestParam("size") int size){
        JwtDTO jwtDTO = JwtUtil.decode(JwtUtil.getToken(headerToken));

        Page<ProfileDTO> profileList = profileService.getProfileList(page, size, jwtDTO);

        return ResponseEntity.ok(profileList);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateByAdmin(@RequestBody ProfileDTO profileDTO, @PathVariable int id, @RequestHeader("Authorization") String headerToken){
        JwtDTO jwtDTO = JwtUtil.decode(JwtUtil.getToken(headerToken));

        String response = profileService.update(profileDTO, id, jwtDTO);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> updateByProfile(@RequestBody ProfileDTO profileDTO, @RequestHeader("Authorization") String headerToken){
        JwtDTO jwtDTO = JwtUtil.decode(JwtUtil.getToken(headerToken));

        String response  = profileService.updateByProfile(profileDTO, jwtDTO);

        return ResponseEntity.ok(response);
    }
}
