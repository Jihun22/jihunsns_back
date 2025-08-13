package com.jihunsns_back.api.dto.response.user;

import com.jihunsns_back.domain.entity.User;

public record UserSummaryRes(
        Long id, String email, String nickname
) {
    public static UserSummaryRes from(User u) {
        return new UserSummaryRes(u.getId(), u.getEmail(), u.getNickname());
    }
}