package com.jihunsns_back.api.service;

import com.jihunsns_back.api.dto.response.post.LikeCountRes;
import com.jihunsns_back.domain.entity.Post;
import com.jihunsns_back.domain.entity.PostLike;
import com.jihunsns_back.domain.entity.User;
import com.jihunsns_back.domain.repository.LikeRepository;
import com.jihunsns_back.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    @Transactional
    public LikeCountRes toggleLike(User user, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        return likeRepository.findByUserAndPost(user, post)
                .map(like -> {
                    likeRepository.delete(like);
                    long count = likeRepository.countByPost_Id(postId);
                    return new LikeCountRes(count, false);
                })
                .orElseGet(() -> {
                    PostLike like = new PostLike(post, user);
                    likeRepository.save(like);
                    long count = likeRepository.countByPost_Id(postId);
                    return new LikeCountRes(count, true);
                });

    }
}