package com.example.demo.config.auth.JsonCustomLogin.filter;

import com.example.demo.domain.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
@Slf4j
// form-login에 사용되는 UsernamePasswordAuthenticationFilter는 AbstractAuthenticationProcessingFilter 를 상속받아 사용
// CustomJsonUsernamePasswordAuthenticationFilter 역시 AbstractAuthenticationProcessingFilter 를 상속받기로 결정
// 로그인 정보를 JSON 형식으로 받아 처리
public class CustomJsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final String DEFAULT_LOGIN_REQUEST_URL = "/api/customer/login" ;
    private static final String HTTP_METHOD = "POST";
    private static final String CONTENT_TYPE = "application/json; charset=UTF-8";
    private static final String USERNAME_KEY = "userId";
    private static final String PASSWORD_KEY = "userPasswd";
    private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD);
    private final ObjectMapper objectMapper;

    //커스텀 로그인 필터의 작동 순서
    // 1. CustomJsonUsernamePasswordAuthenticationFilter ->  2. UsernamePasswordAuthenticationToken ->
    // 3. CustomAuthenticationManager -> 4. CustomUserService , passwordEncoder -> 5. 다시 필터에 UsernamePasswordAuthenticationToken 반환
    // 6. 인증 처리 후 인증 처리된 Authentication 반환
    // 원래 SpringSecurity 에서는 3번 후 DaoAuthenticationProvider에 UsernamePasswordAuthenticationToken 을 전달 후 4번이 작동

    public CustomJsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
        //super 메소드를 통해 부모 클래스인 AbstractAuthenticationProcessingFilter의 생성자에 인자로 DEFAULT_LOGIN_PATH_REQUEST_MATCHER를 전달
        //즉 DEFAULT_LOGIN_PATH_REQUEST_MATCHER에 선언된 url로 어떠한 요청이 오면 로그인 요청으로 인식
        super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER); // 위에서 설정한 "login" + POST로 온 요청을 처리하기 위해 설정
        // 생성자의 파라미터로 ObjectMapper 받기
        this.objectMapper = objectMapper;
    }

    //로그인 필터가 실질적으로 이루어지는 추상 메소드
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        log.info("로그인 커스텀 필터 시작!");

        //요청에서 ContentType가 널 또는 미리 지정한 CONTENT_TYPE 과 다르시 예외처리
        //들어온 요청의 ContentType이 application/json; charset=UTF-8 일 경우 다음 로직 진행
        if(request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)  ) {
            throw new AuthenticationServiceException("Authentication Content-Type not supported: " + request.getContentType());
        }

        // StreamUtils를 통해 받아온 로그인 정보를 꺼내와 messageBody에 저장 (클라이언트 측에서는 아이디와 비밀번호를 json으로 전달한다.)
        // stream은 bytecode이므로 String으로 변환 시 StandardCharsets.UTF_8으로 문자열 인코딩
        String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        // objectMapper 객체를 통해 Json으로 된 정보를 Map의 key(아이디,비밀번호) value(문자열)으로 추출
        Map<String, String> usernamePasswordMap = objectMapper.readValue(messageBody, Map.class);

        //Map으로 저장한 유저아이디 값을 꺼내옵니다.
        String userId = usernamePasswordMap.get(USERNAME_KEY);
        //trim()함수로 빈 공간을 제거
        userId = (userId != null) ? userId : "";
        userId = userId.trim();
        log.info("userId : "+userId);
        //유저 아이드를 처리하는 방식이랑 똑같다.
        String userPasswd = usernamePasswordMap.get(PASSWORD_KEY);
        userPasswd = (userPasswd != null) ? userPasswd : "";
        userPasswd = userPasswd.trim();
        log.info("userPasswd : "+userPasswd);

        //UsernamePasswordAuthenticationToken의 파라미터 principal, credentials에 대입
        // principal = 유저아이디 , credentials에 = 비밀번호
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userId, userPasswd);//principal 과 credentials 전달
        log.info("authRequest : "+this.getAuthenticationManager().authenticate(authRequest));

        //부모(AbstractAuthenticationProcessingFilter)의 getAuthenticationManager()로 AuthenticationManager 객체를 반환
        // authenticate()의 파라미터로 UsernamePasswordAuthenticationToken 객체를 넣고 인증 처리
        //CustomAuthenticationManager로 인해 UsernamePasswordAuthenticationToken 객체는 ROLE를 갖게 됨
        return this.getAuthenticationManager().authenticate(authRequest);
    }

}
