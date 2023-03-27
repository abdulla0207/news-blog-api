package com.company.util;

import com.company.dto.JwtDTO;
import com.company.enums.ProfileRoleEnum;
import io.jsonwebtoken.*;

import java.util.Date;

public class JwtUtil {

    private static final String secretKey = "newsblogsecret";

    // This static method accepts the id and role of the user who wants to login to the website
    // and creates a Token by encoding this information
    public static String encode(Integer profileId, String email, String phoneNumber, ProfileRoleEnum profileRole){
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setIssuedAt(new Date());
        jwtBuilder.signWith(SignatureAlgorithm.HS256, secretKey);
        jwtBuilder.claim("id", profileId);
        jwtBuilder.claim("email", email);
        jwtBuilder.claim("phoneNumber", phoneNumber);
        jwtBuilder.claim("role", profileRole);

        int tokenLifetime = 1000 * 3600 * 24;
        jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + (tokenLifetime)));
        jwtBuilder.setIssuer("NewsBlog");

        String generatedToken = jwtBuilder.compact();

        return generatedToken;
    }

    public static JwtDTO decode(String token){
        try{
            JwtParser jwtParser = Jwts.parser();
            jwtParser.setSigningKey(secretKey);

            Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

            Claims body = claimsJws.getBody();

            Integer id = (Integer) body.get("id");
            ProfileRoleEnum role = ProfileRoleEnum.valueOf((String) body.get("role"));
            String email =(String) body.get("email");
            String phoneNumber = (String) body.get("phoneNumber");

            return new JwtDTO(id, email, phoneNumber, role);
        }catch (JwtException e){
            e.printStackTrace();
        }
        return null;
    }
}
