package com.example.demo.config.auth.Oauth;

import com.example.demo.config.auth.Oauth.Info.GoogleOAuth2UserInfo;
import com.example.demo.config.auth.Oauth.Info.NaverOAuth2UserInfo;
import com.example.demo.config.auth.Oauth.Info.OAuth2UserInfo;
import com.example.demo.domain.Customer;
import com.example.demo.domain.Role;
import com.example.demo.domain.SocialType;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Slf4j
@Getter
// 소셜에 얻어온 데이터를 저장하는 DTO 클래스
// 소셜별로 받는 데이터가 달라 그에 맞게 처리해준다.
public class OAuth2Attribute {
    private Map<String, Object> attributes;
    private String attributeKey;
    OAuth2UserInfo oAuth2UserInfo;

    @Builder
    public OAuth2Attribute(Map<String, Object> attributes, String attributeKey, OAuth2UserInfo oAuth2UserInfo){
        this.attributes=attributes;
        this.attributeKey=attributeKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    // OAuthAttributes 객체를 생성하는 메소드 소셜 타입 별로 해당하는 메소드를 호출하여 소셜에 맞는 OAuthAttributes 객체를 반환
    public static OAuth2Attribute of(SocialType socialType, String attributeKey,
                                     Map<String, Object> attributes){
        if (socialType == SocialType.NAVER) {
            return ofNaver(attributeKey, attributes);
        }
        return ofGoogle(attributeKey,attributes);

    }
    public static OAuth2Attribute ofGoogle(String attributeKey,
                                           Map<String, Object> attributes){
        return OAuth2Attribute.builder()
                .attributes(attributes)
                .attributeKey(attributeKey)
                .oAuth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();

    }
    public static OAuth2Attribute ofNaver(String attributeKey,
                                           Map<String, Object> attributes){
        return OAuth2Attribute.builder()
                .attributes(attributes)
                .attributeKey(attributeKey)
                .oAuth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();

    }
    // OAuth2Attribute 클래스의 필드와 매개변수로 Customer 객체를 반환하는 메소드
    // 해당 메소드가 호출 시 최초 소셜 로그인(회원가입도 진행안 한)한 유저이므로 일단 role를 GUEST로 선언
    public Customer toEntity(SocialType socialType, OAuth2UserInfo oauth2UserInfo) {
        log.info("가져온 이메일: "+oauth2UserInfo.getEmail());
        return Customer.builder()
                .socialType(socialType)
                .socialId(oauth2UserInfo.getId())
                .email(oauth2UserInfo.getEmail())
                .role(Role.GUEST)
                .build();
    }
}
