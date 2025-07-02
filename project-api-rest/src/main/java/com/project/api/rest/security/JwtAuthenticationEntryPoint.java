package com.project.api.rest.security;

import com.project.api.rest.exception.UserUnauthorizedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        // Set the custom response
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        // You can customize the message here
        String message = "Invalid or expired token";

        // If you want to access a specific exception from request attributes:
        Exception ex = (Exception) request.getAttribute("exception");
        if (ex instanceof UserUnauthorizedException) {
            message = ex.getMessage();
        }

        // Write response body
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}

