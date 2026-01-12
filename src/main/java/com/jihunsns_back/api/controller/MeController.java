package com.jihunsns_back.api.controller;

/*
 * ===============================================================
 *  File        : MeController
 *  Author      : yangjihun
 *  Created On  : 26. 1. 9. 오후 4:01
 * ===============================================================
 */

import com.jihunsns_back.domain.entity.User;
import com.jihunsns_back.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.channels.IllegalSelectorException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MeController {


    private final UserRepository userRepository;

    @GetMapping("/me")
    public UserMeRes me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()
                || auth.getPrincipal() == null
                || "anonymousUser".equals(auth.getPrincipal())) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
    String key = auth.getName();

    User u = userRepository.findByEmail(key)
            .orElseThrow(() -> new IllegalStateException("로그인 유저를 찾을 수 없습니다."));

        return new UserMeRes(u.getId(), u.getEmail(), u.getNickname());
}

public record UserMeRes(Long id, String email, String nickname) {}
}