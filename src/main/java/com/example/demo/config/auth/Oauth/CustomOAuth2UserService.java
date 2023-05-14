package com.example.demo.config.auth.Oauth;

import com.example.demo.domain.Customer;
import com.example.demo.domain.Role;
import com.example.demo.domain.SocialType;
import com.example.demo.persistence.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

import static com.example.demo.domain.SocialType.NAVER;

@Slf4j
@RequiredArgsConstructor
@Service
//OAuth2 로그인의 로직을 담당
public class CustomOAuth2UserService  implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final CustomerRepository customerRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{

        //DefaultOAuth2UserService 객체를 생성 후 loadUser메소드를 호출하여 OAuth2User 객체를 생성
        //DefaultOAuth2UserService는 OAuth 2.0 Provider를 제공해준다.
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        /* oAuth2UserServic.loadUser 메소드는 소셜 로그인 시 사용자 정보 제공 URI로 요청(userRequest)한 후
         * 유저의 정보를 얻은 후 DefaultOAuth2User 객체를 생성 후 반환
         * 즉 OAuth2User는 로그인한 유저 정보(사전에 허락한 정보)를 담고 있는 유저이다.
         */
        OAuth2User oAuth2User=oAuth2UserService.loadUser(userRequest);

        // userRequest에 들어있는 소셜을 추출한다.
        // ex) oauth2/authorization/naver에서 registrationId는 naver가 된다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType=getSocialType(registrationId);
        log.info("가져온 소셜타입: "+registrationId);

        /* userNameAttributeName은 로그인 진행시 키가 되는 필드값을 뜻한다.
         * 필드값은 각 계정마다 가지고 있는 유니크한 id값입니다.
         * 구글이면 'sub' 네이버이면 'id'
         */
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // 소셜 로그인에서 API가 제공하는 userInfo의 Json 값들 한마디로 유저 정보의 값들이다.
        // 만약 구글로 로그인 시 sub, name, email, ... 필드와 해당하는 값을 Map으로 반환
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // OAuth2Attribute 객체는 OAuth2 DTO 클래스
        // 로그인한 소셜에 따라 OAuth2Attribute 객체 생성
        OAuth2Attribute oAuth2Attribute =
                OAuth2Attribute.of(socialType, userNameAttributeName, oAuth2User.getAttributes());

        // getCustomer 메소드로 customer 객체 생성
        Customer customer=getCustomer(oAuth2Attribute, socialType);

        // OAuth2User를 사용자 정의한 CustomOAuth2User 객체를 반환한다.
        // 반환 시 Spring에서 알아서 OAuth2LoginAuthenticationToken으로 변환 후 SecurityContextHolder 저장
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(customer.getRole().getKey()))
                ,attributes
                ,oAuth2Attribute.getAttributeKey()
                ,customer.getEmail()
                ,customer.getRole()
                ,customer.getSocialType()
        );
    }
    //소셜 로그인 시 소셜 이름을 얻어오는 메소드
    private SocialType getSocialType(String registrationId) {
        if("naver".equals(registrationId)) {
            return NAVER;
        }
        return SocialType.GOOGLE;
    }
    //소셜 로그인한 유저의 정보로 Customer 객체를 반환하는 메소드
    public Customer getCustomer(OAuth2Attribute oAuth2Attribute, SocialType socialType) {
        Customer findCustomer = customerRepository.findBySocialTypeAndSocialId(socialType, oAuth2Attribute.getOAuth2UserInfo()
                .getId()).orElse(null);
        // null이면 따로 회원가입을 하지 않고 최초로 소셜 로그인하는 유저
        if (findCustomer == null){
            // OAuth2Attribute 객체, socialType으로 새로운 Customer 객체 생성 후 DB에 저장
            return saveUser(oAuth2Attribute, socialType);
        }
        return findCustomer;
    }
    // 새로운 유저를 DB에 저장하는 메소드
    public Customer saveUser(OAuth2Attribute attribute, SocialType socialType){
        //소셜 타입과 이메일로 새로운 Customer 객체 생성 후 저장
        Customer customer=attribute.toEntity(socialType,attribute.getOAuth2UserInfo());
        return customerRepository.save(customer);
    }
}
