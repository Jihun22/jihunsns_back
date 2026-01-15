package com.jihunsns_back.api.dto.response.post;

import com.jihunsns_back.domain.entity.Image;

/**
 * 게시글 이미지 요약 응답.
 */
public record PostImageRes(
        Long id,
        String url,
        String mimeType
) {
    public static PostImageRes from(Image image) {
        if (image == null) {
            return new PostImageRes(null, null, null);
        }
        return new PostImageRes(
                image.getId(),
                image.getUrl(),
                image.getMimeType()
        );
    }
}
