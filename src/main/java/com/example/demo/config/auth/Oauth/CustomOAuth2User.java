package com.example.demo.config.auth.Oauth;

import com.example.demo.domain.Role;
import com.example.demo.domain.SocialType;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

// DefaultOAuth2User를 상속하며 OAuth2User 클래스를 커스텀한 클래스이다.
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {
    // email, role, socialTYPE 필드를 추가로 갖는다.
    // email은 OAuth2 성공 핸들러에 리다이렉트하는 페이지에 이메일 전송하기 위해 필요
    // 또한 해당 이메일로 accessToken를 발급하기 위해 (이메일로 통해 db에 저장된 유저를 파악해서 유저의 아이디를 포함한 accessToken)
    private String email;
    // role 은 소셜 로그인을 처음 이용한 유저가 회원가입을 했는지 아닌지를 파악 여부
    // 회원 가입을 했으면 role은 GUEST -> USER 로 변환
    private Role role;
    private SocialType socialType;



    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            String email, Role role, SocialType socialType){
        super(authorities, attributes, nameAttributeKey);
        this.email=email;
        this.role=role;
        this.socialType=socialType;
    }
}
