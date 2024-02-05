package com.company.controller;

import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRoleEnum;
import com.company.enums.ProfileStatusEnum;
import com.company.exception.ItemAlreadyExistsException;
import com.company.repository.ProfileRepository;
import com.company.service.ConfirmationTokenService;
import com.company.service.EmailService;
import com.company.util.MD5Util;
import com.company.util.MailUtil;
import com.company.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Init Controller is a class that will create initial users, articles and etc.
 * Used one time when we run the application first time
 * It populates the local database with initial records for further usage
 */

@Slf4j
@RestController
@RequestMapping("/api/init")
// First creation of data
public class InitController {

    private final ProfileRepository profileRepository;

    public InitController(ProfileRepository profileRepository){
        this.profileRepository = profileRepository;
    }

    @GetMapping("/admin")
    public String initAdmin(){
        Optional<ProfileEntity> byRole = profileRepository.findProfileEntitiesByRole(ProfileRoleEnum.ADMIN);

        if(byRole.isPresent())
            throw new ItemAlreadyExistsException("Admin already exists in Database");

        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setName("admin");
        profileEntity.setEmail("abdulla.ermatov0407@gmail.com");
        profileEntity.setRole(ProfileRoleEnum.ADMIN);
        profileEntity.setPassword(MD5Util.encode("admin123"));
        profileEntity.setStatus(ProfileStatusEnum.ACTIVE);
        profileEntity.setPhoneNumber("+998943351325");
        profileEntity.setCreatedAt(LocalDateTime.now());
        profileEntity.setUpdatedAt(LocalDateTime.now());
        profileEntity.setSurname("admin");
        profileRepository.save(profileEntity);
        return "Created";
    }
}
