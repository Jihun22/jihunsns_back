// api/dto/request/comment/CommentCreateReq.java
package com.jihunsns_back.api.dto.request.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentCreateReq(
        @NotNull Long postId,
        @NotBlank String content
) {}