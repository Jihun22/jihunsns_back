package com.jihunsns_back.api.dto.request.post;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record PostCreateReq(
        @NotBlank String content,
        List<String> imageUrls
) {
}
