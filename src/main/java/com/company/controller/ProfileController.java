package com.company.controller;

import com.company.dto.ProfileDTO;
import com.company.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService){
        this.profileService = profileService;
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody ProfileDTO profileDTO){
        ProfileDTO responseDTO = profileService.create(profileDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id){
        String s = profileService.deleteById(id);

        return ResponseEntity.ok(s);
    }

    @GetMapping("/")
    public ResponseEntity<?> getList(@RequestParam("page") int page){
        Page<ProfileDTO> profileList = profileService.getProfileList(page);

        return ResponseEntity.ok(profileList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody ProfileDTO profileDTO, @PathVariable int id){
        String response = profileService.update(profileDTO, id);

        return ResponseEntity.ok(response);
    }

}
