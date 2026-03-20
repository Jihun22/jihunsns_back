package com.jihunsns_back.security.oauth2;

/*
 * ===============================================================
 *  File        : NaverOauth2UserInfo
 *  Author      : yangjihun
 *  Created On  : 26. 3. 19. 오후 4:00
 * ===============================================================
 */

import java.util.Map;

/**
 * @class NaverOauth2UserInfo
 * @description 네이버 사용자 파서
 * @author yangjihun
 * @since 26. 3. 19.
 */

public class NaverOauth2UserInfo  implements  Oauth2UserInfo{

    private final  Map<String, Object> attributes;
    private final  Map<String ,Object> response;

    @SuppressWarnings("unchecked")
    public NaverOauth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.response =(Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getProviderId() {
        if (response == null) return  null;
        Object id = response.get("id");
        return id != null ? id.toString() : null;
    }

    @Override
    public String getEmail() {
        if (response == null) return  null;
        Object email = response.get("email");
        return email != null ? email.toString() : null;
    }

    @Override
    public String getName() {
        if (response == null) return "naver_user";
        Object name = response.get("name");
        if (name != null) return name.toString();

        Object nickname = response.get("nickname");
        return nickname != null ? nickname.toString() : "naver_user";
    }

    @Override
    public String getProfileImage() {
        if (response == null) return null;
        Object image = response.get("profile_image");
        return image != null ? image.toString() : null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}