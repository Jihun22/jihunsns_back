package com.jihunsns_back.api.dto.request.post;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record PostUpdateReq(String content , List<MultipartFile> images) {
    public static PostUpdateReq from(String content , List<MultipartFile> images) {
        return new PostUpdateReq(content, images);
    }
}
