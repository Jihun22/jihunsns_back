// api/dto/response/comment/CommentItemRes.java
package com.jihunsns_back.api.dto.response.comment;

import com.jihunsns_back.api.dto.response.user.AuthorRes;
import com.jihunsns_back.domain.entity.Comment;

import java.time.LocalDateTime;

public record CommentItemRes(
        Long id,
        Long postId,
        AuthorRes author,
        String content,
        LocalDateTime createdAt
) {
    public static CommentItemRes from(Comment c) {
        var author = c.getAuthor();
        return new CommentItemRes(
                c.getId(),
                c.getPost().getId(),
                new AuthorRes(author.getId(), author.getNickname()),
                c.getContent(),
                c.getCreatedAt()
        );
    }
}
