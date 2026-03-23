package com.jihunsns_back.security.oauth2;

/*
 * ===============================================================
 *  File        : Oauth2UserInfoFactory
 *  Author      : yangjihun
 *  Created On  : 26. 3. 19. 오후 4:51
 * ===============================================================
 */

import com.jihunsns_back.domain.enums.AuthProvider;

import java.util.Map;

/**
 * @class Oauth2UserInfoFactory
 * @description Provider 판별
 * @author yangjihun
 * @since 26. 3. 19.
 */

public class Oauth2UserInfoFactory {
    private Oauth2UserInfoFactory() {

    }

    public static  Oauth2UserInfo getOauth2UserInfo (AuthProvider authProvider , Map<String,Object> attributes) {
        return switch (authProvider) {
            case GOOGLE -> new GoogleOauth2UserInfo(attributes);
            case KAKAO -> new KakaoOauth2UserInfo(attributes);
            case NAVER -> new NaverOauth2UserInfo(attributes);
            default -> throw new IllegalArgumentException("지원하지 않는 소셜 로그인입니다:" + authProvider);
        };
    }
}