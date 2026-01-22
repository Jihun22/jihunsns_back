package com.jihunsns_back.api.service;

import com.jihunsns_back.domain.entity.Post;
import com.jihunsns_back.domain.entity.PostLike;
import com.jihunsns_back.domain.entity.User;
import com.jihunsns_back.domain.repository.ImageRepository;
import com.jihunsns_back.domain.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

    public boolean toggleLike (Post post , User user) {
        if (likeRepository.existsByUserAndPost(post, user)) {
            likeRepository.deleteByUserAndPost(post, user);
            return false; //좋아요 해제
        }
        likeRepository.save(new PostLike(post, user));
        return  true; //좋아요 추가됨
    }

    public boolean isLiked(User user , Post post) {
        return likeRepository.existsByUserAndPost(post, user);
    }

    public long count(Long postId) {
        return likeRepository.countByPost_Id(postId);
    }

}
