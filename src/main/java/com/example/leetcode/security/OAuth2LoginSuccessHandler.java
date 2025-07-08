package com.example.leetcode.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${frontend.origin}")
    private String frontendOrigin;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        HttpSession session = request.getSession(false);
        String redirectUrl = null;

        if (session != null) {
            redirectUrl = (String) session.getAttribute("redirectUrl");
            session.removeAttribute("redirectUrl");
            log.debug("Found redirectUrl in session: {}", redirectUrl);
        } else {
            log.debug("No session found. Using default redirect.");
        }

        if (redirectUrl == null || !redirectUrl.startsWith("/")) {
            log.warn("Invalid or missing redirectUrl. Falling back to root.");
            redirectUrl = "/";
        }

        String targetUrl = frontendOrigin + redirectUrl;
        log.info("Authentication successful for user: {}. Redirecting to: {}", authentication.getName(), targetUrl);

        response.sendRedirect(targetUrl);
    }
}
