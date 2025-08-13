package com.jihunsns_back.api.service;

import com.jihunsns_back.api.dto.request.post.PostCreateReq;
import com.jihunsns_back.api.dto.response.post.PostItemRes;
import com.jihunsns_back.common.exception.BusinessException;
import com.jihunsns_back.common.exception.ErrorCode;
import com.jihunsns_back.domain.entity.Image;
import com.jihunsns_back.domain.entity.Post;
import com.jihunsns_back.domain.entity.User;
import com.jihunsns_back.domain.repository.ImageRepository;
import com.jihunsns_back.domain.repository.LikeRepository;
import com.jihunsns_back.domain.repository.PostRepository;
import com.jihunsns_back.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepo;
    private final UserRepository userRepo;
    private final ImageRepository imageRepo;
    private final LikeRepository likeRepo;

    @Transactional
    public PostItemRes create(Long userId, PostCreateReq req) {
        User author = userRepo.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "사용자를 찾을 수 없습니다."));
        Post p = new Post();
        p.setAuthor(author);
        p.setContent(req.content());
        Post saved = postRepo.save(p);

        if (req.imageUrls() != null) {
            for (String url : req.imageUrls()) {
                Image img = new Image();
                img.setPost(saved);
                img.setUrl(url);
                imageRepo.save(img);
            }
        }
        long likeCount = likeRepo.countByPostId(saved.getId());
        return PostItemRes.from(saved, likeCount);
    }

    @Transactional(readOnly = true)
    public List<PostItemRes> findAll() {
        List<Post> posts = postRepo.findAll(); // N+1 우려 시 EntityGraph 사용
        return posts.stream()
                .map(p -> PostItemRes.from(p, likeRepo.countByPostId(p.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public PostItemRes findOne(Long id) {
        Post p = postRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
        long likeCount = likeRepo.countByPostId(p.getId());
        return PostItemRes.from(p, likeCount);
    }
}