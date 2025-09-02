package com.jihunsns_back.api.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckNicknameReq {
    @NotBlank(message = "nickname은 필수값입니다.")
    private String nickname;
}