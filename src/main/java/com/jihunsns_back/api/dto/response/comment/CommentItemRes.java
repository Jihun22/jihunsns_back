// api/dto/response/comment/CommentItemRes.java
package com.jihunsns_back.api.dto.response.comment;

import com.jihunsns_back.domain.entity.Comment;

import java.time.LocalDateTime;

public record CommentItemRes(
        Long id,
        Long postId,
        Long authorId,
        String authorNickname,
        String content,
        LocalDateTime createdAt
) {
    public static CommentItemRes from(Comment c) {
        return new CommentItemRes(
                c.getId(), c.getPost().getId(),
                c.getAuthor().getId(), c.getAuthor().getNickname(),
                c.getContent(), c.getCreatedAt()
        );
    }
}