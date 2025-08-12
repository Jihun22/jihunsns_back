package com.jihunsns_back.api.controller;

import com.jihunsns_back.domain.entity.Post;
import com.jihunsns_back.domain.repository.PostRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Post" , description = "게시글 API")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;

    @Operation(summary = "게시글 전체 조회")
    @GetMapping
    public ResponseEntity<List<Post>> findAll() {
        return ResponseEntity.ok(postRepository.findAll());
    }

    @Operation(summary = "게시글 단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<Post> findOne(@PathVariable Long id) {
        return postRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "게시글 생성")
    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post payload) {
        payload.setCreatedAt(LocalDateTime.now());
        Post saved = postRepository.save(payload);
        return ResponseEntity.created(URI.create("/api/posts" + saved.getId())).body(saved);
    }
}
