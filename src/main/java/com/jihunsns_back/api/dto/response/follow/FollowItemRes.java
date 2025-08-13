// api/dto/response/follow/FollowItemRes.java
package com.jihunsns_back.api.dto.response.follow;

import com.jihunsns_back.domain.entity.Follow;

import java.time.LocalDateTime;

public record FollowItemRes(
        Long id, Long followerId, Long followingId, LocalDateTime createdAt
) {
    public static FollowItemRes from(Follow f) {
        return new FollowItemRes(
                f.getId(),
                f.getFollower().getId(),
                f.getFollowing().getId(),
                f.getCreatedAt()
        );
    }
}