package com.jihunsns_back.security.oauth2;

/*
 * ===============================================================
 *  File        : KakaoOauth2UserInfo
 *  Author      : yangjihun
 *  Created On  : 26. 3. 19. 오후 3:35
 * ===============================================================
 */

import java.util.Map;

/**
 * @class KakaoOauth2UserInfo
 * @description 카카오 사용자 파서
 * @author yangjihun
 * @since 26. 3. 19.
 */

public class KakaoOauth2UserInfo implements  Oauth2UserInfo{
     private final Map<String, Object> attributes;

     public KakaoOauth2UserInfo(Map<String,Object> attributes) {
         this.attributes = attributes;
     }

     @Override
    public String getProviderId() {
         Object id = attributes.get("id");
         return id != null ? id.toString() : null;

     }

    @Override
    @SuppressWarnings("unchecked")
    public String getEmail () {
         Map<String, Object> kakaoAccount = (Map<String, Object>)  attributes.get("kakao_account");
         if (kakaoAccount == null)  return null;

         Object email = kakaoAccount.get("email");
         return email != null ? email.toString() : null;
     }

     @Override
    @SuppressWarnings("unchecked")
    public String getName () {
         Map<String, Object> kakaoAccount = (Map<String, Object>)  attributes.get("kakao_account");
         if (kakaoAccount == null) return  "kakao_user";

         Map<String , Object> profile = (Map<String, Object>)  kakaoAccount.get("profile");
         if (profile == null) return "kakao_user";

         Object nickname = profile.get("nickname");
         return nickname != null ? nickname.toString() : "kakao_user";
     }

     @Override
    @SuppressWarnings("unchecked")
    public String getProfileImage() {
         Map<String, Object> kakaoAccount = (Map<String, Object>)  attributes.get("kakao_account");
         if (kakaoAccount == null) return null;

         Map<String , Object> profile = (Map<String, Object>)  kakaoAccount.get("profile");
         if (profile == null) return null;

         Object image = profile.get("profile_image_url");
         return image != null ? image.toString() : null;
     }
     @Override
    public Map<String, Object> getAttributes() {
         return attributes;
     }


}
