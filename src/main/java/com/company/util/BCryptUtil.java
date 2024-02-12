package com.company.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class BCryptUtil {

    // This method encrypts and decrypts the received password and verifies it

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String hashPassword(String password){
        return encoder.encode(password);
    }

    public static boolean verifyPassword(String password, String hashedPassword){
        return encoder.matches(password, hashedPassword);
    }
}
