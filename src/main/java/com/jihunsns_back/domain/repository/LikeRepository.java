package com.jihunsns_back.domain.repository;

import com.jihunsns_back.domain.entity.Post;
import com.jihunsns_back.domain.entity.PostLike;
import com.jihunsns_back.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserAndPost(Post post , User user);

    boolean existsByUserAndPost(Post post , User user);
    void deleteByUserAndPost(Post post , User user);
    long countByPost_Id(Long postId);
}

