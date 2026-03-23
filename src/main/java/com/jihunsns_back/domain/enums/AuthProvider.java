package com.jihunsns_back.domain.enums;

import lombok.Getter;

/*
 * ===============================================================
 *  File        : AuthProvider
 *  Author      : yangjihun
 *  Created On  : 26. 3. 19. 오후 2:21
 * ===============================================================
 */
/**
 * @class AuthProvider
 * @description Oauth 구분값
 * @author yangjihun
 * @since 26. 3. 19.
 */
@Getter
public enum AuthProvider {
    LOCAL("일반가입"),
    GOOGLE("구글"),
    KAKAO("카카오"),
    NAVER("네이버");

    private  final  String description;
    AuthProvider(String description) {
        this.description = description;
    }

}
