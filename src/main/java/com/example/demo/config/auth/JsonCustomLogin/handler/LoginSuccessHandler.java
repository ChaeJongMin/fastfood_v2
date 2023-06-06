package com.example.demo.config.auth.JsonCustomLogin.handler;

import com.example.demo.Service.ConnectCustomerService;
import com.example.demo.Service.CustomerService;
import com.example.demo.Service.RefreshTokenService;
import com.example.demo.config.auth.jwt.JwtTokenProvider;
import com.example.demo.config.auth.jwt.UserData.CustomUserDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
//커스텀 로그인 필터를 정상적으로 통과하여 인증 처리가 되었을 때 즉 로그인 성공 시 작동하는 핸들러
public class LoginSuccessHandler  extends SimpleUrlAuthenticationSuccessHandler  {
    private final ConnectCustomerService connectCustomerService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // 로그인에 성공한 유저의 role를 "role : ROLE_ADMIN or ROLE_USER " 문자열로 전달하기 위해 선언
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("성공 핸들러 메소드 실행");
        // authentication(인증된) 객체로 아이디와 역할을 얻어온다.
        String userId=authentication.getPrincipal().toString();
        String role=authentication.getAuthorities().toString();

        //로그인 했으니 이번 달에 접속한 유저의 수를 증가
        connectCustomerService.increaseCurrentMonthCnt();

        //jwt의 액세스, 리프레쉬 토큰을 생성 및 쿠키로 설정

        //리프레쉬 토큰 value 값을 새로 생성 (refreshToken 테이블에 저장 또는 업데이트)
        String refreshValue=refreshTokenService.save(userId, JwtTokenProvider.REFRESH_TIME).getValue();
        //액세스 토큰과 리프레쉬 토큰을 쿠키에 설정
        jwtTokenProvider.setAccessTokenAndRefreshToken(jwtTokenProvider.generateToken(userId,jwtTokenProvider.ACCESS_TIME),
                refreshValue,response);
        //응답 상태코드 설정 (200 성공0
        response.setStatus(HttpServletResponse.SC_OK);
        //문자열 인코딩 설정
        response.setCharacterEncoding("UTF-8");
        //setContentType타입 JSON으로 설정
        response.setContentType("application/json");

        //유저의 ROLE를 hashMap으로 변환
        HashMap<String, String> successMap=new HashMap<>();
        successMap.put("role",role);

        // objectMapper로 문자열로 변경 (JSON 문자)
        String result=objectMapper.writeValueAsString(successMap);

        //response body message를 생성하여 응답
        response.getWriter().write(result);

    }
}
