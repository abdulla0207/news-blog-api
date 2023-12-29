package com.company.dto;

public record RegistrationDTO(
        String name,
        String surname,
        String email,
        String phoneNumber,
        String password
){}
