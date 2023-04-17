//package com.example.demo.config.auth;
//
//import java.io.IOException;
//import java.net.URLEncoder;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
//import org.springframework.security.authentication.AuthenticationServiceException;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.InternalAuthenticationServiceException;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
//import org.springframework.stereotype.Component;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Component
//public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
//
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        String error="";
//        if(exception  instanceof UsernameNotFoundException){
//            error="존재하지 않는 사용자입니다.";
//        }
//        else if(exception instanceof BadCredentialsException) {
//            request.setAttribute("error", "비밀번호가 틀립니다.");
//            error="아이디 또는 비밀번호가 맞지 않습니다";
//        }
//        else if (exception instanceof InternalAuthenticationServiceException) {
//            error = "내부적으로 발생한 시스템 문제로 인해 요청을 처리할 수 없습니다.";
//        }
//        else if (exception instanceof AuthenticationCredentialsNotFoundException) {
//            error = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
//        }
//        else{
//            error="원인을 모르겠습니다.. 나중에 다시 로그인해 주세요.";
//        }
//        error = URLEncoder.encode(error, "UTF-8");
//        setDefaultFailureUrl("/fastfood/login?error=yes&msg="+error);
//        super.onAuthenticationFailure(request, response, exception);
//    }
//
//}