//package com.example.demo.config.auth.jwt.domain.Util;
//
//import com.example.demo.config.auth.jwt.UserData.CustomUserDetail;
//import com.example.demo.domain.Customer;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//@RequiredArgsConstructor
//public class SecurityUtil {
//    public static String getCurrentMemberId() {
//        System.out.println(SecurityContextHolder.getContext());
//        CustomUserDetail customUserDetail=(CustomUserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if(customUserDetail==null || customUserDetail.getUsername().equals(""))
//            throw new RuntimeException("인증 정보가 없습니다!!");
//        return customUserDetail.getCustomer().getId();
//
//    }
//}
