// api/dto/response/post/LikeCountRes.java
package com.jihunsns_back.api.dto.response.post;

public record LikeCountRes(
        long likeCount,
        boolean liked
) {}