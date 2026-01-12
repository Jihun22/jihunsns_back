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
import com.jihunsns_back.security.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        Object principal = auth.getPrincipal();

        User user = resolveUser(principal);

        return new UserMeRes(user.getId(), user.getEmail(), user.getNickname());
}

    private User resolveUser(Object principal) {
        if (principal instanceof JwtAuthFilter.UserPrincipal jwtPrincipal) {
            return userRepository.findById(jwtPrincipal.id())
                    .orElseThrow(() -> new IllegalStateException("로그인 유저를 찾을 수 없습니다."));
        }
        if (principal instanceof UserDetails userDetails) {
            return userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new IllegalStateException("로그인 유저를 찾을 수 없습니다."));
        }
        if (principal instanceof String email && !"anonymousUser".equals(email)) {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalStateException("로그인 유저를 찾을 수 없습니다."));
        }

        throw new IllegalStateException("로그인 유저를 찾을 수 없습니다.");
    }

public record UserMeRes(Long id, String email, String nickname) {}
}
