// api/dto/response/auth/TokenRes.java
package com.jihunsns_back.api.dto.response.auth;

import java.time.Instant;

public record TokenRes(
        String accessToken,
        String refreshToken,
        Instant accessTokenExpiresAt
) {}