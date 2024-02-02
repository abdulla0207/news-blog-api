package com.company.controller;

import com.company.dto.authentication.AuthResponseDTO;
import com.company.dto.authentication.LoginDTO;
import com.company.dto.authentication.RegistrationDTO;
import com.company.service.AuthorizationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class is only used, when in the USER side.
 * For creation of an account and registration
 */

@RestController
@RequestMapping("/api/auth")
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    @Autowired
    public AuthorizationController(AuthorizationService authorizationService){
        this.authorizationService = authorizationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registration(@Valid @RequestBody RegistrationDTO registrationDTO){
        String response = authorizationService.signup(registrationDTO);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO){
        AuthResponseDTO responseDTO = authorizationService.login(loginDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token){
        String response = authorizationService.confirmToken(token);

        return ResponseEntity.ok(response);
    }
}
