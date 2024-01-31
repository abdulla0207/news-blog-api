package com.company.util;

import com.company.entity.ConfirmationTokenEntity;
import com.company.service.ConfirmationTokenService;

import java.time.LocalDateTime;
import java.util.UUID;

public class TokenUtil {
    public static String saveTokenForUser(Integer id, ConfirmationTokenService tokenService) {
        String token = UUID.randomUUID().toString();
        ConfirmationTokenEntity tokenEntity = new ConfirmationTokenEntity();
        tokenEntity.setProfileId(id);
        tokenEntity.setToken(token);
        tokenEntity.setCreatedAt(LocalDateTime.now());
        tokenEntity.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        tokenService.saveConfirmationToken(tokenEntity);

        return token;
    }
}
