// api/dto/response/post/PostDetailRes.java
package com.jihunsns_back.api.dto.response.post;

import com.jihunsns_back.api.dto.response.comment.CommentItemRes;
import com.jihunsns_back.domain.entity.Comment;
import com.jihunsns_back.domain.entity.Image;
import com.jihunsns_back.domain.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

public record PostDetailRes(
        Long id,
        String content,
        Long authorId,
        String authorNickname,
        List<String> images,
        long likeCount,
        boolean liked,
        List<CommentItemRes> comments,
        LocalDateTime createdAt
) {
    public static PostDetailRes from(Post p, long likeCount, boolean liked) {
        List<String> imgs = p.getImages() == null ? List.of()
                : p.getImages().stream().map(Image::getUrl).toList();

        List<CommentItemRes> cs = p.getComments() == null ? List.of()
                : p.getComments().stream().map(CommentItemRes::from).toList();

        return new PostDetailRes(
                p.getId(), p.getContent(),
                p.getAuthor().getId(), p.getAuthor().getNickname(),
                imgs, likeCount, liked, cs, p.getCreatedAt()
        );
    }
}