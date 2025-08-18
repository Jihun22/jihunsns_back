package com.jihunsns_back.api.controller;

import com.jihunsns_back.api.dto.request.post.PostCreateReq;
import com.jihunsns_back.api.dto.response.post.PostItemRes;
import com.jihunsns_back.api.service.PostService;
import com.jihunsns_back.security.auth.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Post" , description = "게시글 API")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 전체 조회")
    @GetMapping
    public ResponseEntity<List<PostItemRes>> findAll() {
        return ResponseEntity.ok(postService.findAll());
    }

    @Operation(summary = "게시글 단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<PostItemRes> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(postService.findOne(id));
    }

    @Operation(summary = "게시글 생성")
    @PostMapping
    public ResponseEntity<PostItemRes> create(
            @CurrentUser Long userId,   // ← 명확하게 현재 유저 ID임을 드러냄
            @Valid @RequestBody PostCreateReq payload
    ) {
        PostItemRes saved = postService.create(userId, payload);
        return ResponseEntity
                .created(URI.create("/api/posts/" + saved.id()))
                .body(saved);
    }
}