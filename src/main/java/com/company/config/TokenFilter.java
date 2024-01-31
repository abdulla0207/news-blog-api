package com.company.config;

import com.company.dto.JwtDTO;
import com.company.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Configuration
public class TokenFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        System.out.println("TokenFilter doFilter");

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();

        // This if statement will not check for token "/article/type/language/" endpoint
        switch (requestURI){
            case "/api/article/types/language", "/api/regions/language", "/api/categories/language":
                filterChain.doFilter(request, response);
                return;
        }
        // Gets the Token from the header
        final String authHeader = request.getHeader("Authorization");

        // Checks if the string is not null or starts with Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("Message", "Token Not Found.");
            return;
        }

        // Refactors the token and removes the word "Bearer " from the text
        String token = authHeader.substring(7);


        JwtDTO jwtDto;
        try {
            // Decodes the token to JwtDTO object
            jwtDto = JwtUtil.decode(token);
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("Message", "Token Not Valid");
            return;
        }


        request.setAttribute("id", jwtDto.id());
        request.setAttribute("role", jwtDto.role());
        filterChain.doFilter(request, response);
    }

}
