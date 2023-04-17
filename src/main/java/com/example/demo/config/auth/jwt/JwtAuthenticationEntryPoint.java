package com.example.demo.config.auth.jwt;

import com.example.demo.exception.CustomException;
import com.example.demo.exception.ErrorResponse;
import com.example.demo.exception.errorCode.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.demo.exception.errorCode.ErrorCode.*;
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
//        String exception=(String)request.getAttribute("exception");
        String exception="test";
        log.info("JwtAuthenticationEntryPoint: "+response.getStatus()+" ");
        if(exception.equals(ACCESS_TOKEN_EXPIRED.name())){
            log.info("액세스 토큰이 만료되어 응답값에 설정");
            setResponse(response, ACCESS_TOKEN_EXPIRED);
        }
        else if(exception.equals(FAIL_TOKEN_CHECK.name())){
            setResponse(response, FAIL_TOKEN_CHECK);
        }
        else if(exception.equals(MISMATCH_USERNAME_TOKEN.name())){
            setResponse(response, MISMATCH_USERNAME_TOKEN);
        }
//        else{
//            setResponse(response, AUTHORIZED_DENIED);
//        }
    }
    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());
        JSONObject responseJson = new JSONObject();
        responseJson.put("message", errorCode.getDetail());
        responseJson.put("code", errorCode.name());
        response.getWriter().print(responseJson);
    }
}