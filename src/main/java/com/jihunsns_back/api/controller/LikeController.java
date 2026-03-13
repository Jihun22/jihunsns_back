package com.jihunsns_back.api.controller;

import com.jihunsns_back.api.dto.response.post.LikeCountRes;
import com.jihunsns_back.api.service.LikeService;
import com.jihunsns_back.common.exception.BusinessException;
import com.jihunsns_back.common.exception.ErrorCode;
import com.jihunsns_back.domain.entity.User;
import com.jihunsns_back.domain.repository.UserRepository;
import com.jihunsns_back.security.jwt.JwtAuthFilter;
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
    private final UserRepository userRepository;

    @Operation(summary = "좋아요 토글")
    @PostMapping("/{postId}")
    public LikeCountRes toggleLike(
            @PathVariable Long postId,
            Authentication authentication
    ) {
        JwtAuthFilter.UserPrincipal principal = (JwtAuthFilter.UserPrincipal) authentication.getPrincipal();

        User user = userRepository.findById(principal.id())
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED, "유저 없음"));

        return likeService.toggleLike(user, postId);
    }
}