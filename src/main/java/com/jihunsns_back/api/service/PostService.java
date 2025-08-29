// src/main/java/com/jihunsns_back/api/service/PostService.java
package com.jihunsns_back.api.service;

import com.jihunsns_back.api.dto.request.post.PostCreateReq;
import com.jihunsns_back.api.dto.request.post.PostUpdateReq;
import com.jihunsns_back.api.dto.response.post.PostItemRes;
import com.jihunsns_back.common.exception.BusinessException;
import com.jihunsns_back.common.exception.ErrorCode;
import com.jihunsns_back.domain.entity.Post;
import com.jihunsns_back.domain.entity.User;
import com.jihunsns_back.domain.repository.LikeRepository;
import com.jihunsns_back.domain.repository.PostRepository;
import com.jihunsns_back.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public PostItemRes create(Long userId, PostCreateReq req) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "작성자 없음"));

        Post post = new Post();
        String content = req.content() == null ? "" : req.content().trim();
        post.setContent(content);
        post.setAuthor(author);

        Post saved = postRepository.save(post);
        long likeCount = likeRepository.countByPost_Id(saved.getId());
        return PostItemRes.from(saved, likeCount);
    }

    public Page<PostItemRes> findAll(Pageable pageable) {
        return postRepository.findAllBy(pageable)
                .map(p -> PostItemRes.from(p, likeRepository.countByPost_Id(p.getId())));
    }

    public PostItemRes findOne(Long id) {
        Post p = postRepository.findWithAuthorAndImagesById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND, "게시글 없음"));
        long likeCount = likeRepository.countByPost_Id(id);
        return PostItemRes.from(p, likeCount);
    }

    public Page<PostItemRes> findByUser(Long userId, Pageable pageable) {
        return postRepository.findByAuthor_Id(userId, pageable)
                .map(p -> PostItemRes.from(p, likeRepository.countByPost_Id(p.getId())));
    }

    @Transactional
    public PostItemRes update(Long me, Long postId, PostUpdateReq req) {
        Post post = postRepository.findWithAuthorAndImagesById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND, "게시글 없음"));

        if (!post.getAuthor().getId().equals(me)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "작성자만 수정할 수 있습니다.");
        }

        String content = req.content() == null ? "" : req.content().trim();
        post.setContent(content); // 더티체킹

        long likeCount = likeRepository.countByPost_Id(post.getId());
        return PostItemRes.from(post, likeCount);
    }

    @Transactional
    public void delete(Long me, Long postId) {
        Post post = postRepository.findWithAuthorAndImagesById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND, "게시글 없음"));

        if (!post.getAuthor().getId().equals(me)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }

    public Page<PostItemRes> followFeed(Long me, Pageable pageable) {

        return null;
    }
}