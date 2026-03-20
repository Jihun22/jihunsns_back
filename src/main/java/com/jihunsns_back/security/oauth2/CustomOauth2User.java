package com.jihunsns_back.security.oauth2;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/*
 * ===============================================================
 *  File        : CustomOauth2User
 *  Author      : yangjihun
 *  Created On  : 26. 3. 19. 오후 4:59
 * ===============================================================
 */
@Getter
public class CustomOauth2User implements OAuth2User {

    private  final  OAuth2User oAuth2User;
    private  final  Long userId;

    public CustomOauth2User(OAuth2User oAuth2User , Long userId) {
        this.oAuth2User = oAuth2User;
        this.userId = userId;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return  oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2User.getName();
    }
}