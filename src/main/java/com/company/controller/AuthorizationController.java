package com.company.controller;

import com.company.dto.authentication.AuthResponseDTO;
import com.company.dto.authentication.LoginDTO;
import com.company.dto.authentication.RegistrationDTO;
import com.company.enums.LanguageEnum;
import com.company.service.AuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Registration of a new user")
    @PostMapping("/signup")
    public ResponseEntity<String> registration(@Valid @RequestBody RegistrationDTO registrationDTO, @RequestHeader("Accept-Language") String lang){
        log.info("Registration {}", registrationDTO);
        String response = authorizationService.signup(registrationDTO, lang);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Login of a user to the system")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO, @RequestHeader("Accept-Language") LanguageEnum lang){
        log.info("Log in {}", loginDTO);
        AuthResponseDTO responseDTO = authorizationService.login(loginDTO, lang);

        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "Confirmation endpoint for new users", description = "This endpoint creates a link for user confirmation and it sent and used in emails of users to confirm registration")
    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token, @RequestHeader("Accept-Language") LanguageEnum lang){
        log.info("confirmation token");
        String response = authorizationService.confirmToken(token, lang);

        return ResponseEntity.ok(response);
    }
}
