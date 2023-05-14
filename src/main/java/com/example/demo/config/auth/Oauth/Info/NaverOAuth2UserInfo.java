package com.example.demo.config.auth.Oauth.Info;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
@Slf4j
public class NaverOAuth2UserInfo extends OAuth2UserInfo {
    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    //naver은 이메일과 아이디가 reponse 로 감싸져있어 get("reponse")로 꺼내야한다.

    @Override
    public String getId() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        if (response == null) {
            return null;
        }
        return (String) response.get("id");
    }

    @Override
    public String getEmail(){
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        log.info("가져온 이메일: "+(String) response.get("email"));
        if (response == null) {
            return null;
        }
        return (String) response.get("email");
    }

}
