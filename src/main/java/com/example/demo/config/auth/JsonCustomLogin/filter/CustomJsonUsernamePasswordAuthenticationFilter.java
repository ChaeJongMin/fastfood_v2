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
public class CustomJsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final String DEFAULT_LOGIN_REQUEST_URL = "/api/customer/login" ;
    private static final String HTTP_METHOD = "POST";
    private static final String CONTENT_TYPE = "application/json; charset=UTF-8";
    private static final String USERNAME_KEY = "userId";
    private static final String PASSWORD_KEY = "userPasswd";
    private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD);

    private final ObjectMapper objectMapper;
    public CustomJsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
        super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER); // 위에서 설정한 "login" + POST로 온 요청을 처리하기 위해 설정
        this.objectMapper = objectMapper;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        log.info("로그인 커스텀 필터 시작!");

        if(request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)  ) {
            throw new AuthenticationServiceException("Authentication Content-Type not supported: " + request.getContentType());
        }

        String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        Map<String, String> usernamePasswordMap = objectMapper.readValue(messageBody, Map.class);

        String userId = usernamePasswordMap.get(USERNAME_KEY);
        userId = (userId != null) ? userId : "";
        userId = userId.trim();
        log.info("userId : "+userId);
        String userPasswd = usernamePasswordMap.get(PASSWORD_KEY);
        userPasswd = (userPasswd != null) ? userPasswd : "";
        userPasswd = userPasswd.trim();
        log.info("userPasswd : "+userPasswd);


//        new UsernamePasswordAuthenticationToken(userId, userPasswd, userDetails.getAuthorities());
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userId, userPasswd);//principal 과 credentials 전달
        log.info("authRequest : "+this.getAuthenticationManager().authenticate(authRequest));
        return this.getAuthenticationManager().authenticate(authRequest);
    }

}
