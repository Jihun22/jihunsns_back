package com.jihunsns_back.api.dto.response.user;

import com.jihunsns_back.domain.entity.User;

import java.time.LocalDateTime;

public record UserProfileRes(
        Long id, String email, String nickname, String role, LocalDateTime createdAt
) {
    public static UserProfileRes from(User u) {
        return new UserProfileRes(u.getId(), u.getEmail(), u.getNickname(), u.getRole(), u.getCreatedAt());
    }
}