package com.jihunsns_back.security.oauth2;

/*
 * ===============================================================
 *  File        : GoogleOauth2UserInfo
 *  Author      : yangjihun
 *  Created On  : 26. 3. 19. 오후 3:18
 * ===============================================================
 */

import java.util.Map;

/**
 * @class GoogleOauth2UserInfo
 * @description 구글 사용자 정보 파서
 * @author yangjihun
 * @since 26. 3. 19.
 */
public class GoogleOauth2UserInfo implements Oauth2UserInfo {

    private final Map<String, Object> attributes;

    public GoogleOauth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public  String getProviderId() {
        Object sub = attributes.get("sub");
        return sub != null ? sub.toString() : null;
    }

    @Override
    public String getEmail() {
        Object email = attributes.get("email");
        return  email != null ? email.toString() : null;
    }

    @Override
    public String getName() {
        Object name = attributes.get("name");
        return name != null ? name.toString() : "google_user";
    }

    @Override
    public String getProfileImage() {
        Object picture = attributes.get("picture");
        return picture != null ? picture.toString() : null;
    }

    @Override
    public  Map<String , Object> getAttributes() {
        return attributes;
    }

}