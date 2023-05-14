package com.example.demo.config.auth.Oauth;

import com.example.demo.Service.CustomerService;
import com.example.demo.Service.RefreshTokenService;
import com.example.demo.config.auth.jwt.JwtTokenProvider;
import com.example.demo.config.auth.jwt.domain.RefreshTokens;
import com.example.demo.config.auth.jwt.domain.dto.RefreshTokenDto;
import com.example.demo.domain.Customer;
import com.example.demo.domain.Role;
import com.example.demo.persistence.CustomerRepository;
import com.example.demo.persistence.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CookieValue;

import javax.persistence.Id;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Component
//OAuth2 로그인이 성공하면 OAuth2LoginSuccessHandler의 로직이 실행
// SecurityConfig에 설정 필요
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final CustomerRepository customerRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomerService customerService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        log.info("OAuth2 로그인 성공!! -> 현재 성공핸들러입니다.");
        try{
            // authentication.getPrincipal() 메소드를 통해 CustomOAuth2User 객체를 얻어온다.
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            // 현재 찾아온 유저가 역할이 GUEST 이면
            if(oAuth2User.getRole()== Role.GUEST){
                log.info("신규회원입니다(이메일).: "+oAuth2User.getEmail());
                response.sendRedirect("http://localhost:8080/fastfood/register?mail="+oAuth2User.getEmail());
            }
            // 이미 존재하는 회원일 경우
            else{
                log.info("기존회원입니다.");
                //토큰 발급 메소드
                loginSuccess(response,oAuth2User);
                response.sendRedirect("http://localhost:8080/fastfood/menu");
            }
        } catch (Exception e){
            throw e;
        }

    }
    private void loginSuccess(HttpServletResponse response, CustomOAuth2User customOAuth2User){
        response.setStatus(HttpServletResponse.SC_OK);
        // customOAuth2User로 이메일을 얻어와 Customer 객체 얻어오기
        Customer customer=customerService.findByEmailToCustomer(customOAuth2User.getEmail());
        // 토큰 발급을 위해 따로 아이디만 추출
        String userId=customer.getUserId();
        String refreshValue="";
        //해당 유저의 리프레쉬 토큰이 존재 여부를 통해 다시 리프레쉬 토큰을 발급할지 아닐지 결정

        // DB에 해당 유저의 refreshToken이 존재할 시
        if(refreshTokenService.findRefreshTokenbyUser(userId)){
            log.info("로그인한 유저의 리프레쉬 토큰이 db에 존재");
            // refreshToken 값을 따로 추출
            RefreshTokenDto refreshTokenDto=refreshTokenService.findTokenById(userId);
            refreshValue=refreshTokenDto.getValue();
            //이 리프레쉬 토큰이 만료일 경우
            if(!refreshTokenService.checkExpireTime(refreshTokenService.findTokenById(userId).getExpiration())){
                log.info("새로운 리프레쉬 토큰 업데이트");
                //새로 리프레쉬 토큰을 생성 후 변수에 저장
                String newRefreshToken=jwtTokenProvider.refreshGenerateToken(userId,jwtTokenProvider.REFRESH_TIME);
                //update 메소드를 통해 토큰 값과 만료기간을 업데이트
                refreshValue=refreshTokenService.update(userId,
                        newRefreshToken,
                        jwtTokenProvider.getExpiredTime(newRefreshToken));
            }
        }
        // 새로 리프레쉬 토큰 생성
        else{
            log.info("새로운 리프레쉬 토큰 생성");
            refreshValue=refreshTokenService.save(userId, JwtTokenProvider.REFRESH_TIME).getValue();
        }
        // ACCESS 토큰 생성 후 reponse에 해당 토큰들을 쿠키에 설정
        jwtTokenProvider.setAccessTokenAndRefreshToken(jwtTokenProvider.generateToken(userId,jwtTokenProvider.ACCESS_TIME),
                refreshValue,response);

        log.info("loginSuccess 완료");
    }


}
