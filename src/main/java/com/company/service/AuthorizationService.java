package com.company.service;

import com.company.dto.RegistrationDTO;
import com.company.dto.auth.AuthResponseDTO;
import com.company.dto.auth.LoginDTO;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRoleEnum;
import com.company.enums.ProfileStatusEnum;
import com.company.exception.EmailException;
import com.company.exception.ItemNotFoundException;
import com.company.exception.ProfileCreateException;
import com.company.repository.ProfileRepository;
import com.company.util.JwtUtil;
import com.company.util.MD5Util;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthorizationService {
    private ProfileRepository profileRepository;

    public AuthorizationService(ProfileRepository profileRepository){
        this.profileRepository = profileRepository;
    }
    public String registration(RegistrationDTO registrationDTO) {
        Optional<ProfileEntity> byEmail = profileRepository.findByEmail(registrationDTO.getEmail());

        if(byEmail.isPresent()){
            throw new EmailException("Profile with this email already exist");
        }
        if(registrationDTO.getName().isEmpty() || registrationDTO.getName().isBlank())
            throw new ProfileCreateException("Name is empty. Please indicate your name");
        if(registrationDTO.getSurname().isEmpty() || registrationDTO.getSurname().isBlank())
            throw new ProfileCreateException("Surname is empty. Please indicate your surname");
        if(!registrationDTO.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$"))
            throw new ProfileCreateException("Password should have at least one number, one lowercase or uppercase character" +
                    " and the length should be at least 8 letters");
        if(!registrationDTO.getPhoneNumber().matches("[+]998[0-9]{9}"))
            throw new ProfileCreateException("Phone number should be in the following format: +998 xx xxx-xx-xx");


        ProfileEntity profileEntity = new ProfileEntity();

        profileEntity.setPhoneNumber(registrationDTO.getPhoneNumber());
        profileEntity.setName(registrationDTO.getName());
        profileEntity.setSurname(registrationDTO.getSurname());
        profileEntity.setEmail(registrationDTO.getEmail());
        profileEntity.setStatus(ProfileStatusEnum.ACTIVE);
        profileEntity.setRole(ProfileRoleEnum.USER);
        profileEntity.setPassword(MD5Util.encode(registrationDTO.getPassword()));
        profileEntity.setUpdatedAt(LocalDateTime.now());
        profileEntity.setCreatedAt(LocalDateTime.now());

        profileRepository.save(profileEntity);
        return "Profile Added";
    }

    public AuthResponseDTO login(LoginDTO loginDTO) {
        Optional<ProfileEntity> getProfile = profileRepository.findByEmailAndPhoneNumberAndPassword(loginDTO.getEmail(),
                loginDTO.getPhoneNumber(), MD5Util.encode(loginDTO.getPassword()));

        if(getProfile.isEmpty())
            throw new ItemNotFoundException("Email, phonenumber or password is incorrect");

        ProfileEntity profileEntity = getProfile.get();
        if(profileEntity.getStatus().equals(ProfileStatusEnum.BLOCKED))
            throw new RuntimeException("Profile is blocked");

        AuthResponseDTO responseDTO = new AuthResponseDTO();
        responseDTO.setName(profileEntity.getName());
        responseDTO.setRole(profileEntity.getRole());
        responseDTO.setSurname(profileEntity.getSurname());
        responseDTO.setToken(JwtUtil.encode(profileEntity.getId(), profileEntity.getEmail(), profileEntity.getPhoneNumber(), profileEntity.getRole()));

        return responseDTO;
    }
}
