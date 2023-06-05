//package com.example.demo.config.auth;
//
//
//import com.example.demo.domain.Customer;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.stereotype.Component;
//
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class CustomAuthenticationProvider implements AuthenticationProvider {
//    private final CustomLoadUserByUsername customLoadUserByUsername;
//
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        Customer user = (Customer) customLoadUserByUsername.loadUserByUsername(authentication.getName().toString());
//        String reqPassword = authentication.getCredentials().toString();
//        System.out.println("CustomAuthenticationProvider: "+authentication.getName());
//        //if(!passwordEncoder.matches(reqPassword, passwordEncoder.encode(user.getUserPasswd()))) throw new BadCredentialsException("Not Found User");
//        if(!passwordEncoder.matches(reqPassword, user.getUserPasswd())) throw new BadCredentialsException("Not Found User");
//        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//        authorities.add(new SimpleGrantedAuthority(user.getRole().getKey()));
////        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getId(), null, authorities));
////        return SecurityContextHolder.getContext().getAuthentication();
//        return new UsernamePasswordAuthenticationToken(user.getId(), null, authorities);
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return true;
//    }
//
//}