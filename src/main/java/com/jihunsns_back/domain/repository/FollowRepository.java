package com.jihunsns_back.domain.repository;

import com.jihunsns_back.domain.entity.Follow;
import com.jihunsns_back.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    // 팔로잉 존재 여부 확인
    boolean existsByFollowerAndFollowing(User follower, User following);

    // 팔로우 관계 조회
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    // 내가 팔로우한 사람들
    List<Follow> findByFollower(User follower);

    // 나를 팔로우한 사람들
    List<Follow> findByFollowing(User following);

    // 언팔로우 처리
    void deleteByFollowerAndFollowing(User follower, User following);
}