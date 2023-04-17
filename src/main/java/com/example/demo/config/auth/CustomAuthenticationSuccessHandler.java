//package com.example.demo.config.auth;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Component
//public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        HttpSession session = request.getSession();
//        session.setMaxInactiveInterval(360);
//        List<String> roleNames = new ArrayList<>();
//        authentication.getAuthorities().forEach(authority->{
//            roleNames.add(authority.getAuthority());
//        });
//        System.out.println(roleNames.get(0) );
//        if(roleNames.get(0).equals("ROLE_USER")) {
//            System.out.println("유저");
//            response.sendRedirect("http://localhost:8080/fastfood/menu");
//        }
//        else if(roleNames.get(0).equals("ROLE_ADMIN")) {
//            System.out.println("관리자");
//            response.sendRedirect("http://localhost:8080/fastfood/superhome");
//        }
//    }
//
//
//}