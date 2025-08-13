// api/dto/request/comment/CommentUpdateReq.java
package com.jihunsns_back.api.dto.request.comment;

import jakarta.validation.constraints.NotBlank;

public record CommentUpdateReq(
        @NotBlank String content
) {}