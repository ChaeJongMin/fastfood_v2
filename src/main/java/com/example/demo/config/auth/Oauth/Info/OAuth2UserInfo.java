package com.example.demo.config.auth.Oauth.Info;

import java.util.Map;

// 소셜 별로 유저의 정보를 담고 있는 추상 클래스
// 소셜에 맞는 클래스가 해당 추상 클래스를 상속 받아 사용
public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId(); //소셜 식별 값 : 구글 - "sub", 카카오 - "id", 네이버 - "id"
    public abstract String getEmail();

}
