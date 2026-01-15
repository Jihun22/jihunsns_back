// src/main/java/com/jihunsns_back/api/service/PostService.java
package com.jihunsns_back.api.service;

import com.jihunsns_back.api.dto.request.post.PostCreateReq;
import com.jihunsns_back.api.dto.request.post.PostUpdateReq;
import com.jihunsns_back.api.dto.response.post.PostItemRes;
import com.jihunsns_back.common.exception.BusinessException;
import com.jihunsns_back.common.exception.ErrorCode;
import com.jihunsns_back.domain.entity.Image;
import com.jihunsns_back.domain.entity.Post;
import com.jihunsns_back.domain.entity.User;
import com.jihunsns_back.domain.repository.LikeRepository;
import com.jihunsns_back.domain.repository.PostRepository;
import com.jihunsns_back.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    @Value("${app.public-base-url:}")
    private String publicBaseUrl;

    @Transactional
    public PostItemRes create(Long userId, PostCreateReq req) {
        return createMultipart(userId, req.content(), req.images());
    }

    @Transactional
    public PostItemRes createMultipart(Long userId, String content, List<MultipartFile> images) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "작성자 없음"));

        Post post = new Post();
        post.setContent(normalizeContent(content));
        post.setAuthor(author);

        attachImages(post, images);

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
        return updateMultipart(me, postId, req.content(), req.images());
    }

    @Transactional
    public PostItemRes updateMultipart(Long me, Long postId, String content, List<MultipartFile> images) {
        Post post = postRepository.findWithAuthorAndImagesById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND, "게시글 없음"));

        if (!post.getAuthor().getId().equals(me)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "작성자만 수정할 수 있습니다.");
        }

        post.setContent(normalizeContent(content));
        replaceImages(post, images);

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

        clearImages(post);
        postRepository.delete(post);
    }

    public Page<PostItemRes> followFeed(Long me, Pageable pageable) {
        return null;
    }

    private void attachImages(Post post, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return;
        }
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                continue;
            }
            String url = storeFile(file);
            Image image = new Image();
            image.setUrl(url);
            image.setMimeType(file.getContentType());
            image.setPost(post);
            post.getImages().add(image);
        }
    }

    private void replaceImages(Post post, List<MultipartFile> files) {
        if (files == null) {
            return; // 이미지 파트가 없으면 기존 이미지를 유지
        }
        clearImages(post);
        attachImages(post, files);
    }

    private void clearImages(Post post) {
        List<Image> current = new ArrayList<>(post.getImages());
        for (Image image : current) {
            deleteStoredFile(image.getUrl());
        }
        post.getImages().clear();
    }

    private String storeFile(MultipartFile file) {
        try {
            Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(dir);

            String original = file.getOriginalFilename();
            String cleaned = original == null ? "" : StringUtils.cleanPath(original);
            String ext = "";
            int idx = cleaned.lastIndexOf('.');
            if (idx >= 0) {
                ext = cleaned.substring(idx);
            }
            String storedName = UUID.randomUUID().toString().replace("-", "") + ext;
            Path target = dir.resolve(storedName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return buildFileUrl(storedName);
        } catch (IOException e) {
            log.error("이미지 저장 실패: {}", file.getOriginalFilename(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "이미지 저장에 실패했습니다.");
        }
    }

    private void deleteStoredFile(String url) {
        if (!StringUtils.hasText(url)) {
            return;
        }
        int idx = url.lastIndexOf('/');
        String filename = idx >= 0 ? url.substring(idx + 1) : url;
        if (!StringUtils.hasText(filename)) {
            return;
        }
        try {
            Path target = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(filename);
            Files.deleteIfExists(target);
        } catch (IOException e) {
            log.warn("이미지 삭제 실패: {}", filename, e);
        }
    }

    private String normalizeContent(String raw) {
        return raw == null ? "" : raw.trim();
    }

    private String buildFileUrl(String filename) {
        String prefix = StringUtils.hasText(publicBaseUrl) ? publicBaseUrl : "";
        if (StringUtils.hasText(prefix) && prefix.endsWith("/")) {
            prefix = prefix.substring(0, prefix.length() - 1);
        }
        return prefix + "/uploads/" + filename;
    }
}
