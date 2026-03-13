package com.jihunsns_back.domain.repository;

import com.jihunsns_back.domain.entity.Post;
import com.jihunsns_back.domain.entity.PostLike;
import com.jihunsns_back.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByUserAndPost(User user, Post post);

    boolean existsByUserAndPost(User user, Post post);

    @Modifying(clearAutomatically = true , flushAutomatically = true)

    void deleteByUserAndPost(User user, Post post);

    long countByPost_Id(Long postId);
}