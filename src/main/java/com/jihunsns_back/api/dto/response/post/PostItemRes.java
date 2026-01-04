// api/dto/response/post/PostItemRes.java
package com.jihunsns_back.api.dto.response.post;

import com.jihunsns_back.api.dto.response.user.AuthorRes;
import com.jihunsns_back.domain.entity.Image;
import com.jihunsns_back.domain.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

public record PostItemRes(
        Long id,
        String content,
        AuthorRes author,
        List<String> images,
        long likeCount,
        LocalDateTime createdAt
) {
    public static PostItemRes from(Post p, long likeCount) {
        List<String> imgs = p.getImages() == null ? List.of()
                : p.getImages().stream().map(Image::getUrl).toList();
        return new PostItemRes(
                p.getId(),
                p.getContent(),
                new AuthorRes(
                        p.getAuthor().getId(),
                        p.getAuthor().getNickname()
                ),
                imgs,
                likeCount,
                p.getCreatedAt()
        );
    }
}