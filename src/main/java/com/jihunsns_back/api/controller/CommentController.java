package com.jihunsns_back.api.controller;

import com.jihunsns_back.api.dto.request.comment.CommentCreateReq;
import com.jihunsns_back.api.dto.request.comment.CommentUpdateReq;
import com.jihunsns_back.api.dto.response.comment.CommentItemRes;
import com.jihunsns_back.api.service.CommentService;
import com.jihunsns_back.domain.entity.Comment;
import com.jihunsns_back.domain.entity.Post;
import com.jihunsns_back.domain.entity.User;
import com.jihunsns_back.domain.repository.PostRepository;
import com.jihunsns_back.domain.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * ===============================================================
 *  File        : CommentController
 *  Author      : yangjihun
 *  Created On  : 26. 1. 9. 오후 2:42
 * ===============================================================
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //댓글 작성
    @Operation(summary = "댓글 작성")
    @PostMapping
    public CommentItemRes create(@Valid @RequestBody CommentCreateReq req) {
        User me = getCurrentUser();

        Post post = postRepository.findById(req.postId())
                .orElseThrow(() -> new IllegalArgumentException("계시글이 존재하지 않습니다."));

        Comment c = new Comment();
        c.setPost(post);
        c.setAuthor(me);
        c.setContent(req.content().trim());

        return CommentItemRes.from(commentService.save(c));


    }

    //댓글 목록 Get
    @Operation(summary = "댓글목록 가져오기 ")
    @GetMapping
    public List<CommentItemRes> list(@RequestParam Long postId, Pageable pageable) {
        return commentService.findByPostId(postId, pageable)
                .stream()
                .map(CommentItemRes::from)
                .toList();
    }

    //댓글 수정 PATCH

    @Operation(summary = "댓글 수정")
    @PatchMapping("/{commentId}")
    public CommentItemRes update(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateReq req
    ) {
        User me = getCurrentUser();

        Comment c = commentService
                .findById(commentId);
        if (!c.getAuthor().getId().equals(me.getId())) {
            throw new IllegalStateException("수정 권한이 없습니다.");

        }
        c.setContent(req.content().trim());
        return CommentItemRes.from(commentService.save(c));
    }

    //댓글 삭제 DELETE
    @Operation(summary = "댓글 삭제")

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long commentId) {
        User me = getCurrentUser();

        Comment c = commentService.findById(commentId);
        if (!c.getAuthor().getId().equals(me.getId())) {
            throw new IllegalStateException("삭제 권한이 없습니다.");

        }
        commentService.deleteById(commentId);
    }

    /*
    현재 로그인 유저 가져오기
     */

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("AUTH name ={}" , auth != null ? auth.getName() : null);
        log.info("AUTH principalClass={}" , auth != null && auth.getPrincipal()!=null ? auth.getPrincipal().getClass() : null);
        log.info("AUTH principal={}", auth != null ? auth.getPrincipal() : null);
        if (auth == null || !auth.isAuthenticated()
                || auth.getPrincipal() == null
                || "anonymousUser".equals(auth.getPrincipal())) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        Object principal = auth.getPrincipal();


        // ✅ JwtAuthFilter가 심어준 principal에서 userId로 조회 (핵심)
        if (principal instanceof com.jihunsns_back.security.jwt.JwtAuthFilter.UserPrincipal p) {
            return userRepository.findById(p.id())
                    .orElseThrow(() -> new IllegalStateException("로그인 유저를 찾을 수 없습니다."));
        }

        // (혹시 모를 fallback) principal이 문자열로 들어오는 케이스 대응
        if (principal instanceof String s && s.matches("^\\d+$")) {
            return userRepository.findById(Long.parseLong(s))
                    .orElseThrow(() -> new IllegalStateException("로그인 유저를 찾을 수 없습니다."));
        }

        throw new IllegalStateException("로그인 유저를 찾을 수 없습니다.");
    }
}