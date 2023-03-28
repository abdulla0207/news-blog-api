package com.company.controller;

import com.company.dto.authentication.AuthResponseDTO;
import com.company.dto.authentication.LoginDTO;
import com.company.dto.RegistrationDTO;
import com.company.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {
    private AuthorizationService authorizationService;

    @Autowired
    public AuthorizationController(AuthorizationService authorizationService){
        this.authorizationService = authorizationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registration(@RequestBody RegistrationDTO registrationDTO){
        String response = authorizationService.registration(registrationDTO);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
        AuthResponseDTO responseDTO = authorizationService.login(loginDTO);

        return ResponseEntity.ok(responseDTO);
    }
}
