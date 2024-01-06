package com.company.service;

import com.company.dto.RegistrationDTO;
import com.company.dto.authentication.AuthResponseDTO;
import com.company.dto.authentication.LoginDTO;
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
    private final ProfileRepository profileRepository;

    public AuthorizationService(ProfileRepository profileRepository){
        this.profileRepository = profileRepository;
    }
    public String signup(RegistrationDTO registrationDTO) {
        Optional<ProfileEntity> byEmail = profileRepository.findByEmail(registrationDTO.email());
        Optional<ProfileEntity> byPhoneNumber = profileRepository.findByPhoneNumber(registrationDTO.phoneNumber());
        if(byEmail.isPresent() || byPhoneNumber.isPresent()){
            throw new EmailException("Profile with this email or phone number already exist");
        }
        //Validating the object that came from user side
        if(registrationDTO.name().isEmpty() || registrationDTO.name().isBlank())
            throw new ProfileCreateException("Name is empty. Please indicate your name");
        if(registrationDTO.surname().isEmpty() || registrationDTO.surname().isBlank())
            throw new ProfileCreateException("Surname is empty. Please indicate your surname");
        if(!registrationDTO.password().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$"))
            throw new ProfileCreateException("Password should have at least one number, one lowercase or uppercase character" +
                    " and the length should be at least 8 letters");
        if(!registrationDTO.phoneNumber().matches("[+]998[0-9]{9}"))
            throw new ProfileCreateException("Phone number should be in the following format: +998 xx xxx-xx-xx");

        //Creating an entity object by setting values from user side
        ProfileEntity profileEntity = new ProfileEntity();

        profileEntity.setPhoneNumber(registrationDTO.phoneNumber());
        profileEntity.setName(registrationDTO.name());
        profileEntity.setSurname(registrationDTO.surname());
        profileEntity.setEmail(registrationDTO.email());
        profileEntity.setStatus(ProfileStatusEnum.ACTIVE);
        profileEntity.setRole(ProfileRoleEnum.USER);
        profileEntity.setPassword(MD5Util.encode(registrationDTO.password()));
        profileEntity.setUpdatedAt(LocalDateTime.now());
        profileEntity.setCreatedAt(LocalDateTime.now());

        profileRepository.save(profileEntity);
        return "Profile Added";
    }

    public AuthResponseDTO login(LoginDTO loginDTO) {
        Optional<ProfileEntity> getProfile = profileRepository.findByEmailAndPhoneNumberAndPassword(loginDTO.getEmail(),
                loginDTO.getPhoneNumber(), MD5Util.encode(loginDTO.getPassword()));

        if(getProfile.isEmpty())
            throw new ItemNotFoundException("Email, phone number or password is incorrect");

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
