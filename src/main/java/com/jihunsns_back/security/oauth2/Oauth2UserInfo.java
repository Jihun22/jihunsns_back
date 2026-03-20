package com.jihunsns_back.security.oauth2;

/*
 * ===============================================================
 *  File        : Oauth2UserInfo
 *  Author      : yangjihun
 *  Created On  : 26. 3. 19. 오후 2:55
 * ===============================================================
 */

import java.util.Map;

/**
 * @class Oauth2UserInfo
 * @description Oauth 사용자 공통 인터페이스
 * @author yangjihun
 * @since 26. 3. 19.
 */

public interface Oauth2UserInfo {
    String getProviderId();
    String getEmail();
    String getName();
    String getProfileImage();

    Map<String,Object> getAttributes();
}
