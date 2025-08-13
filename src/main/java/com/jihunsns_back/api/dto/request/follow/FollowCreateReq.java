// api/dto/request/follow/FollowCreateReq.java
package com.jihunsns_back.api.dto.request.follow;

import jakarta.validation.constraints.NotNull;

public record FollowCreateReq(
        @NotNull Long targetUserId
) {}