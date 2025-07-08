package com.example.leetcode.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public Map<String, Object> getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            log.info("Anonymous access to /api/users/me");
            return Map.of("authenticated", false);
        }

        String login = getAttribute(principal, "login");
        String name = getAttribute(principal, "name");
        String email = getAttribute(principal, "email");
        String avatarUrl = getAttribute(principal, "avatar_url");

        log.info("Authenticated user: {}", login);
        log.debug("User details - name: {}, email: {}, avatar: {}", name, email, avatarUrl);

        Map<String, Object> userInfo = new LinkedHashMap<>();
        userInfo.put("authenticated", true);
        userInfo.put("username", login);
        userInfo.put("name", name);
        userInfo.put("email", email);
        userInfo.put("avatar", avatarUrl);

        return userInfo;
    }

    private String getAttribute(OAuth2User user, String key) {
        Object value = user.getAttribute(key);
        return value != null ? value.toString() : "";
    }
}
