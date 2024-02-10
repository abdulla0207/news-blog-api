package com.company.service;

import com.company.dto.authentication.RegistrationDTO;
import com.company.dto.authentication.AuthResponseDTO;
import com.company.dto.authentication.LoginDTO;
import com.company.entity.ConfirmationTokenEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.LanguageEnum;
import com.company.enums.ProfileRoleEnum;
import com.company.enums.ProfileStatusEnum;
import com.company.exception.*;
import com.company.repository.ProfileRepository;
import com.company.util.JwtUtil;
import com.company.util.MD5Util;
import com.company.util.MailUtil;
import com.company.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class AuthorizationService {
    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ConfirmationTokenService tokenService;
    private final ResourceMessageService resourceMessageService;

    @Autowired
    public AuthorizationService(ProfileRepository profileRepository, EmailService emailService,
                                ConfirmationTokenService tokenService, ResourceMessageService resourceMessageService){
        this.profileRepository = profileRepository;
        this.emailService = emailService;
        this.tokenService =tokenService;
        this.resourceMessageService = resourceMessageService;
    }
    public String signup(RegistrationDTO registrationDTO, String lang) {
        Optional<ProfileEntity> byEmail = profileRepository.findByEmail(registrationDTO.email());
        Optional<ProfileEntity> byPhoneNumber = profileRepository.findByPhoneNumber(registrationDTO.phoneNumber());
        if(byEmail.isPresent() || byPhoneNumber.isPresent()){
            log.info("User with email or phone number exist");
            throw new EmailException(resourceMessageService.getMessage("user.email.phone.exist", lang));
        }

        //Creating an entity object by setting values from user side
        ProfileEntity profileEntity = new ProfileEntity();

        profileEntity.setPhoneNumber(registrationDTO.phoneNumber());
        profileEntity.setName(registrationDTO.name());
        profileEntity.setSurname(registrationDTO.surname());
        profileEntity.setEmail(registrationDTO.email());
        profileEntity.setStatus(ProfileStatusEnum.NOT_ACTIVE);
        profileEntity.setRole(ProfileRoleEnum.USER);
        profileEntity.setPassword(MD5Util.encode(registrationDTO.password()));
        profileEntity.setUpdatedAt(LocalDateTime.now());
        profileEntity.setCreatedAt(LocalDateTime.now());

        profileRepository.save(profileEntity);

        String token = TokenUtil.saveTokenForUser(profileEntity.getId(), tokenService);

        // Sending verification mail to users email.
        String message = MailUtil.mailMessage(registrationDTO.name(), registrationDTO.surname(), token);

        emailService.sendMail(registrationDTO.email(), message, lang);
        return token;
    }

    public AuthResponseDTO login(LoginDTO loginDTO, LanguageEnum lang) {
        Optional<ProfileEntity> getProfile = profileRepository.findByEmailAndPhoneNumberAndPassword(loginDTO.getEmail(),
                loginDTO.getPhoneNumber(), MD5Util.encode(loginDTO.getPassword()));

        if(getProfile.isEmpty()) {
            log.info("Email, phone number or password is incorrect");
            throw new ItemNotFoundException(resourceMessageService.getMessage("user.login.field.incorrect", lang.name()));
        }

        ProfileEntity profileEntity = getProfile.get();
        if(profileEntity.getStatus().equals(ProfileStatusEnum.BLOCKED)) {
            log.info("Account blocked");
            throw new RuntimeException(resourceMessageService.getMessage("user.blocked", lang.name()));
        }

        if(profileEntity.getStatus().equals(ProfileStatusEnum.NOT_ACTIVE)){
            String token = TokenUtil.saveTokenForUser(profileEntity.getId(), tokenService);
            String message = MailUtil.mailMessage(profileEntity.getName(), profileEntity.getSurname(), token);

            emailService.sendMail(profileEntity.getEmail(), message, lang.name());
            AuthResponseDTO responseDTO = new AuthResponseDTO();
            responseDTO.setResendVerification(true);
            return responseDTO;
        }

        AuthResponseDTO responseDTO = new AuthResponseDTO();
        responseDTO.setName(profileEntity.getName());
        responseDTO.setRole(profileEntity.getRole());
        responseDTO.setSurname(profileEntity.getSurname());
        responseDTO.setToken(JwtUtil.encode(profileEntity.getId(), profileEntity.getEmail(), profileEntity.getPhoneNumber(), profileEntity.getRole()));

        return responseDTO;
    }

    public String confirmToken(String token, LanguageEnum lang){
        Optional<ConfirmationTokenEntity> token1 = tokenService.getToken(token);
        if(token1.isEmpty()) {
            log.warn("Token not found");
            throw new ItemNotFoundException(resourceMessageService.getMessage("token.not.found", lang.name()));
        }

        ConfirmationTokenEntity tokenEntity = token1.get();

        if(tokenEntity.getConfirmedAt() != null) {
            log.info("Account already confirmed");
            throw new TokenAlreadyConfirmedException(resourceMessageService.getMessage("account.confirm", lang.name()));
        }

        LocalDateTime expiresAt = tokenEntity.getExpiresAt();

        if(expiresAt.isBefore(LocalDateTime.now())) {
            log.info("Token expired");
            throw new TokenNotValidException(resourceMessageService.getMessage("token.expire", lang.name()));
        }

        tokenService.setConfirmedAt(token);
        profileRepository.activateProfileStatus(tokenEntity.getProfileEntity().getEmail());

        return resourceMessageService.getMessage("confirm", lang.name());
    }
}
