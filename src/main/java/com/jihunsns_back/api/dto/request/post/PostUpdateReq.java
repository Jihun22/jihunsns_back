package com.jihunsns_back.api.dto.request.post;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record PostUpdateReq(
        @NotBlank String content,
        List<String> imageUrls // 교체 정책(전체 교체 vs 증감)은 서비스 규약으로 결정
) {}
