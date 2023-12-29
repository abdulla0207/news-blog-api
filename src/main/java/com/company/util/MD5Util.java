package com.company.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    // This method encrypts the received password in MD5
    public static String encode(String password){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(password.getBytes());
            StringBuffer encryptedVersion = new StringBuffer();

            for (byte b : digest) {
                encryptedVersion.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
            }

            return encryptedVersion.toString();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }
}
