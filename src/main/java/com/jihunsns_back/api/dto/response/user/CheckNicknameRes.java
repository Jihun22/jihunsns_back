package com.jihunsns_back.api.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckNicknameRes {
    private boolean exists;
}