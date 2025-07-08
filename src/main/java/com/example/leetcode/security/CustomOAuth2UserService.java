package com.example.leetcode.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.example.leetcode.model.User;
import com.example.leetcode.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oauthUser = delegate.loadUser(req);

        Number githubIdRaw = oauthUser.getAttribute("id");
        Long githubId = githubIdRaw != null ? githubIdRaw.longValue() : null;
        String login = oauthUser.getAttribute("login");
        String name = oauthUser.getAttribute("name");
        String avatar = oauthUser.getAttribute("avatar_url");

        log.info("OAuth2 user login: login={}, id={}", login, githubId);

        String email = fetchPrimaryEmailFromGithub(req.getAccessToken().getTokenValue());

        if (email != null) {
            log.debug("Fetched primary verified email for user {}: {}", login, email);
        } else {
            log.warn("Could not fetch verified primary email for GitHub user: {}", login);
        }

        if (githubId != null) {
            repository.findById(githubId).orElseGet(() -> {
                log.info("New user. Saving to DB: login={}, email={}", login, email);
                return repository.save(new User(githubId, login, email, name, avatar));
            });
        } else {
            log.error("GitHub ID is null — user will not be persisted");
        }

        Map<String, Object> attributes = new HashMap<>(oauthUser.getAttributes());
        if (email != null) {
            attributes.put("email", email);
        }

        return new DefaultOAuth2User(oauthUser.getAuthorities(), attributes, "login");
    }

    private String fetchPrimaryEmailFromGithub(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    "https://api.github.com/user/emails",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {
                    });

            List<Map<String, Object>> emails = response.getBody();
            if (emails != null) {
                for (Map<String, Object> emailEntry : emails) {
                    Boolean primary = (Boolean) emailEntry.get("primary");
                    Boolean verified = (Boolean) emailEntry.get("verified");
                    if (Boolean.TRUE.equals(primary) && Boolean.TRUE.equals(verified)) {
                        return (String) emailEntry.get("email");
                    }
                }
            } else {
                log.warn("GitHub email response body is null");
            }
        } catch (Exception e) {
            log.error("Error fetching email from GitHub: {}", e.getMessage(), e);
        }
        return null;
    }
}
