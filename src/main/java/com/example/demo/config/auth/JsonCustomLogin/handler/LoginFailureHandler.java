package com.example.demo.config.auth.JsonCustomLogin.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("로그인 실패 핸들러! " +exception.toString());
        String error="";

        if(exception  instanceof UsernameNotFoundException){
            error="존재하지 않는 사용자입니다.";
        }
        else if(exception instanceof BadCredentialsException) {
            request.setAttribute("error", "비밀번호가 틀립니다.");
            error="아이디 또는 비밀번호가 맞지 않습니다";
        }
        else if (exception instanceof InternalAuthenticationServiceException) {
            error = "내부적으로 발생한 시스템 문제로 인해 요청을 처리할 수 없습니다.";
        }
        else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            error = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
        }
        else if(exception instanceof AuthenticationServiceException){
            error = "contentType이 옳지 않습니다.";
        }
        else{
            error="원인을 모르겠습니다.. 나중에 다시 로그인해 주세요.";
        }
//        error = URLEncoder.encode(error, "UTF-8");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        HashMap<String, String> errorMap=new HashMap<>();
        errorMap.put("error",error);

        String result=objectMapper.writeValueAsString(errorMap);

        response.getWriter().write(result);
//        setDefaultFailureUrl("/fastfood/login?error=yes&msg="+error);
//        super.onAuthenticationFailure(request, response, exception);
    }
}
