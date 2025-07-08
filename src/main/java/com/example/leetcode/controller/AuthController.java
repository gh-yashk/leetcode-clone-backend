package com.example.leetcode.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login/oauth2")
    public void loginWithRedirect(
            @RequestParam(required = false, defaultValue = "/") String redirectUrl,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        log.info("🔐 Received login request with redirectUrl: {}", redirectUrl);

        HttpSession session = request.getSession(true);
        session.setAttribute("redirectUrl", redirectUrl);
        log.debug("🗂️  Stored redirectUrl in session [id={}]: {}", session.getId(), redirectUrl);

        String githubAuthEndpoint = "/oauth2/authorization/github";
        log.info("➡️  Redirecting to GitHub OAuth2 authorization: {}", githubAuthEndpoint);

        response.sendRedirect(githubAuthEndpoint);
    }
}
