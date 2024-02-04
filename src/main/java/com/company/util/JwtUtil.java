package com.company.util;

import com.company.dto.JwtDTO;
import com.company.enums.ProfileRoleEnum;
import com.company.exception.AppForbiddenException;
import com.company.exception.TokenNotValidException;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;

public class JwtUtil {

    private static final String secretKey = "newsblogsecret";

    // This static method accepts the id, email, phone number and role of the user who wants to login to the website
    // and creates a Token by encoding this information by using JWT
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

        return jwtBuilder.compact();
    }

    // Decodes the token to Jwt object
    public static JwtDTO decode(String token){
        JwtParser jwtParser = Jwts.parser();
        jwtParser.setSigningKey(secretKey);

        Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

        Claims body = claimsJws.getBody();

        Integer id = (Integer) body.get("id");
        ProfileRoleEnum role = ProfileRoleEnum.valueOf((String) body.get("role"));

        return new JwtDTO(id, role);
    }

    public static Integer getIdFromHeader(HttpServletRequest request){
        try {
            Integer userId = (Integer) request.getAttribute("id");
            return userId;
        }catch (RuntimeException e){
            throw new TokenNotValidException("Unauthorized");
        }
    }

    public static JwtDTO getJwtDTO(HttpServletRequest request){
        try {
            int id = (Integer) request.getAttribute("id");
            ProfileRoleEnum roleEnum = (ProfileRoleEnum) request.getAttribute("role");

            return new JwtDTO(id, roleEnum);
        }catch (RuntimeException e){
            throw new TokenNotValidException("Unauthorized");
        }
    }

    public static boolean checkForRole(HttpServletRequest request, ProfileRoleEnum roleEnum){
            ProfileRoleEnum adminRole = (ProfileRoleEnum) request.getAttribute("role");
            if(!adminRole.equals(roleEnum)){
                throw new AppForbiddenException("Method Not Allowed");
            }

            return true;
    }
}
