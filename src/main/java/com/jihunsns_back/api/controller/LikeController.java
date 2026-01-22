package com.jihunsns_back.api.controller;

import com.jihunsns_back.api.dto.response.post.LikeCountRes;
import com.jihunsns_back.api.service.LikeService;
import com.jihunsns_back.common.exception.BusinessException;
import com.jihunsns_back.common.exception.ErrorCode;
import com.jihunsns_back.domain.entity.Post;
import com.jihunsns_back.domain.entity.PostLike;
import com.jihunsns_back.domain.entity.User;
import com.jihunsns_back.domain.repository.LikeRepository;
import com.jihunsns_back.domain.repository.PostRepository;
import com.jihunsns_back.domain.repository.UserRepository;
import com.jihunsns_back.security.jwt.JwtAuthFilter;
import io.jsonwebtoken.Jwt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/*
 * ===============================================================
 *  File        : LikeController
 *  Author      : yangjihun
 *  Created On  : 26. 1. 22. 오후 4:30
 * ===============================================================
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
@Tag(name = "좋아요", description = "게시글 좋아요 API")

public class LikeController {
    private final LikeService likeService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    @Operation(summary = "좋아요 토글")
    @PostMapping("/{postId}")
    public LikeCountRes toggleLike(
            @PathVariable Long postId,
            Authentication authentication
    ) {
        JwtAuthFilter.UserPrincipal principal = getPrincipal(authentication);

        User user = userRepository.findById(principal.id())
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED,"유저 없음"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND ,"게시글 없음"));

        //이미 눌렀는지 확인
        boolean liked = likeRepository.existsByUserAndPost(post, user);

        if (liked) {
            likeRepository.deleteByUserAndPost(post, user);
        } else {
            likeRepository.save(new PostLike(post,user));
        }
        long count = likeRepository.countByPost_Id(postId);
        return new LikeCountRes(count, !liked);
    }


    // 좋아요 개수 조회
    @Operation(summary = "좋아요 상태 조회 ")
    @GetMapping("/{postId}")
    public LikeCountRes getLikeStatus(
            @PathVariable Long postId,
            Authentication authentication
    ) {
        JwtAuthFilter.UserPrincipal principal = (JwtAuthFilter.UserPrincipal) authentication.getPrincipal();

        User user = userRepository.findById(principal.id())
                .orElseThrow(() -> new IllegalStateException("유저 없음"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException("게시글 없음"));

        boolean liked = likeRepository.existsByUserAndPost(post, user);
        long count = likeRepository.countByPost_Id(postId);

        return new LikeCountRes(count, liked);

    }
    private JwtAuthFilter.UserPrincipal getPrincipal(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof  JwtAuthFilter.UserPrincipal userPrincipal)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED ,"인증되지 않습니다.");

        }
        return userPrincipal;
    }
}