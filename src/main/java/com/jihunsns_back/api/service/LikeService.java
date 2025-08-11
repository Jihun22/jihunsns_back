package com.jihunsns_back.api.service;

import com.jihunsns_back.domain.entity.Like;
import com.jihunsns_back.domain.entity.Post;
import com.jihunsns_back.domain.entity.User;
import com.jihunsns_back.domain.repository.ImageRepository;
import com.jihunsns_back.domain.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final ImageRepository imageRepository;
    private final LikeRepository likeRepository;

    public Like save(Like like) {
        return likeRepository.save(like);
    }

    public void delete(Like like) {
        likeRepository.delete(like);
    }

    public boolean existsByPostAndUser(Post post , User user){
        return likeRepository.existsByPostAndUser(post,user);
    }

}
