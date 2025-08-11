package com.jihunsns_back.domain.repository;

import com.jihunsns_back.domain.entity.Like;
import com.jihunsns_back.domain.entity.Post;
import com.jihunsns_back.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {

    // 특정 유저가 특정 게시글에 누른 좋아요 조회
    Optional<Like> findByUserAndPost(User user, Post post);

    // 특정 게시글에 달린 좋아요 전체 조회
    List<Like> findByPost(Post post);

    // 특정 유저가 누른 좋아요 전체 조회
    List<Like> findByUser(User user);

    // 특정 유저가 특정 게시글에 좋아요 눌렀는지 여부 확인
    boolean existsByUserAndPost(User user, Post post);

    // 특정 유저가 특정 게시글의 좋아요 취소용
    void deleteByUserAndPost(User user, Post post);

    boolean existsByUserAndPostAndUser(User user, Post post, User user1);

    boolean existsByPostAndUser(Post post, User user);
}