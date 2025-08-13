
package com.jihunsns_back.api.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupReq(
        @Email @NotBlank String email,
        @NotBlank @Size(min = 8, max = 60) String password,
        @NotBlank @Size(max = 50) String nickname
) {}