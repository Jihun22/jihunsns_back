package com.jihunsns_back.api.dto.request.post;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record PostCreateReq(String content, List<MultipartFile> images) {
    public static PostCreateReq from(String content , List<MultipartFile> images) {
        return new PostCreateReq(content, images);
    }
}
