package com.jihunsns_back.api.service;

/*
 * ===============================================================
 *  File        : CustomOauth2UserService
 *  Author      : yangjihun
 *  Created On  : 26. 3. 19. 오후 5:11
 * ===============================================================
 */

import com.jihunsns_back.domain.entity.User;
import com.jihunsns_back.domain.enums.AuthProvider;
import com.jihunsns_back.security.oauth2.CustomOauth2User;
import com.jihunsns_back.security.oauth2.Oauth2UserInfo;
import com.jihunsns_back.security.oauth2.Oauth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * @class CustomOauth2UserService
 * @description Oauth 커스텀 서비스
 * @author yangjihun
 * @since 26. 3. 19.
 */
@Service
@RequiredArgsConstructor
public class CustomOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        AuthProvider authProvider = AuthProvider.valueOf(registrationId.toUpperCase());

        Oauth2UserInfo userInfo = Oauth2UserInfoFactory.getOauth2UserInfo(authProvider,oAuth2User.getAttributes());

        String providerId = userInfo.getProviderId();
        String email = userInfo.getEmail();
        String name =userInfo.getName();
        String profileImage = userInfo.getProfileImage();

        if (providerId == null || providerId.isBlank()) {
            throw new OAuth2AuthenticationException("providerId를 가져올 수 없습니다.");

        }

        User user = userService.findByAuthProviderAndProviderId(authProvider,providerId)
                .orElseGet(() -> registerSocialUser(authProvider, providerId , email, name, profileImage));

        updateUserInfoIfNeeded(user,email,name,profileImage);

        return new CustomOauth2User(oAuth2User, user.getId());

    }
    private  User registerSocialUser(
            AuthProvider authProvider,
            String providerId,
            String email,
            String name,
            String profileImage
    ){
        User user = new User();
        user.setAuthProvider(authProvider);
        user.setProviderId(providerId);
        user.setEmail(email);
        user.setNickname(generateUniqueNickname(name,authProvider));
        user.setProfileImage(profileImage);
        user.setPassword(null);
        user.setRole("ROLE_USER");
        return userService.save(user);
    }

    private void updateUserInfoIfNeeded(User user, String email, String name, String profileImage) {
        boolean updated = false;

        if ((user.getEmail() == null || user.getEmail().isBlank()) && email != null && !email.isBlank()){
            user.setEmail(email);
            updated = true;
        }
        if ((user.getProfileImage() == null || user.getProfileImage().isBlank()) && profileImage != null && !profileImage.isBlank()){
            user.setProfileImage(profileImage);
            updated = true;
        }
        if ((user.getNickname() == null || user.getNickname().isBlank()) && name != null && !name.isBlank()){
            user.setNickname(generateUniqueNickname(name,user.getAuthProvider()));
            updated = true;
        }
        if (updated) {
            userService.save(user);
        }
    }

    private String generateUniqueNickname(String name, AuthProvider authProvider) {
        String base = (name == null || name.isBlank())
                ? authProvider.name().toLowerCase() + "_user"
                : name.trim().replaceAll("\\s+", "");

        if (base.length() > 20) {
            base = base.substring(0,20);
        }
        String candidate = base;
        int index = 1;

        while (userService.existsByNickname(candidate)) {
            candidate = base + index;
            if (candidate.length() > 50) {
            candidate = candidate.substring(0,50);
        }
        index++;
    }
    return candidate;

}
}