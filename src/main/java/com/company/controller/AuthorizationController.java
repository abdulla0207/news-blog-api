package com.company.controller;

import com.company.dto.authentication.AuthResponseDTO;
import com.company.dto.authentication.LoginDTO;
import com.company.dto.authentication.RegistrationDTO;
import com.company.enums.LanguageEnum;
import com.company.service.AuthorizationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class is only used, when in the USER side.
 * For creation of an account and registration
 */

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth API list", description = "API list for authorization and authentication")
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    @Autowired
    public AuthorizationController(AuthorizationService authorizationService){
        this.authorizationService = authorizationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registration(@Valid @RequestBody RegistrationDTO registrationDTO, @RequestHeader("Accept-Language") String lang){
        log.info("Registration {}", registrationDTO);
        String response = authorizationService.signup(registrationDTO, lang);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO, @RequestHeader("Accept-Language") LanguageEnum lang){
        log.info("Log in {}", loginDTO);
        AuthResponseDTO responseDTO = authorizationService.login(loginDTO, lang);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token, @RequestHeader("Accept-Language") LanguageEnum lang){
        log.info("confirmation token");
        String response = authorizationService.confirmToken(token, lang);

        return ResponseEntity.ok(response);
    }
}
