package com.example.demo.config.auth.Oauth.Info;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {
    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }
    @Override
    //id를 반환
    public String getId() {
        return (String) attributes.get("sub");
    }
    @Override
    //이메일을 반환
    public String getEmail(){return (String) attributes.get("email"); }

}
