// src/main/java/com/jihunsns_back/api/controller/PostController.java
package com.jihunsns_back.api.controller;

import com.jihunsns_back.api.dto.request.post.PostCreateReq;
import com.jihunsns_back.api.dto.request.post.PostUpdateReq;
import com.jihunsns_back.api.dto.response.post.PostItemRes;
import com.jihunsns_back.api.service.PostService;
import com.jihunsns_back.common.response.ApiResponse;
import com.jihunsns_back.common.response.PageResponse;
import com.jihunsns_back.security.auth.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@Tag(name = "Post" , description = "게시글 API")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 전체 조회 (페이지네이션)")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PostItemRes>>> findAll(
            @ParameterObject Pageable pageable // ?page=0&size=10&sort=createdAt,desc
    ) {
        Page<PostItemRes> page = postService.findAll(pageable);
        return ResponseEntity.ok(ApiResponse.ok(PageResponse.of(page)));
    }

    @Operation(summary = "게시글 단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostItemRes>> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(postService.findOne(id)));
    }

    @Operation(summary = "특정 사용자 게시글 조회 (페이지네이션)")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<PageResponse<PostItemRes>>> findByUser(
            @PathVariable Long userId,
            @ParameterObject Pageable pageable
    ) {
        Page<PostItemRes> page = postService.findByUser(userId, pageable);
        return ResponseEntity.ok(ApiResponse.ok(PageResponse.of(page)));
    }

    @Operation(summary = "팔로우 피드 조회 (내가 팔로우한 사용자의 글, 페이지네이션)")
    @GetMapping("/feed")
    public ResponseEntity<ApiResponse<PageResponse<PostItemRes>>> feed(
            @CurrentUser Long me,
            @ParameterObject Pageable pageable
    ) {
        Page<PostItemRes> page = postService.followFeed(me, pageable);
        return ResponseEntity.ok(ApiResponse.ok(PageResponse.of(page)));
    }

    @Operation(summary = "게시글 생성")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PostItemRes>> create(
            @CurrentUser Long me,
            @RequestPart("content") String content,
            @RequestPart(value = "image", required = false) List<MultipartFile> images
    ) {
        PostItemRes saved = postService.createMultipart(me, content, images);
        return ResponseEntity
                .created(URI.create("/api/posts/" + saved.id()))
                .body(ApiResponse.ok(saved));
    }

    @Operation(summary = "게시글 수정 (작성자만)")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PostItemRes>> update(
            @CurrentUser Long me,
            @PathVariable Long id,
            @RequestPart("content") String content,
            @RequestPart(value = "image", required = false) List<MultipartFile> images
    ) {
        return ResponseEntity.ok(ApiResponse.ok(postService.updateMultipart(me, id, content, images)));
    }
    @Operation(summary = "게시글 삭제 (작성자만)")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @CurrentUser Long me,
            @PathVariable Long id
    ) {
        postService.delete(me, id);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
