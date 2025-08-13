// api/dto/request/auth/LoginReq.java
package com.jihunsns_back.api.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginReq(
        @Email @NotBlank String email,
        @NotBlank String password
) {}