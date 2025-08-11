package com.jihunsns_back.api.service;

import com.jihunsns_back.domain.entity.Follow;
import com.jihunsns_back.domain.entity.User;
import com.jihunsns_back.domain.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;

    public Follow follow(User follower, User following) {
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        return followRepository.save(follow);
    }

    public void unfollow(User follower, User following) {
        followRepository.deleteByFollowerAndFollowing(follower, following);
    }

    public boolean isFollowing(User follower, User following) {
        return followRepository.existsByFollowerAndFollowing(follower, following);
    }
}
