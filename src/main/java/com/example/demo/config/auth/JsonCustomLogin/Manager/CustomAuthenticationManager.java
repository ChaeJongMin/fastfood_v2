package com.example.demo.config.auth.JsonCustomLogin.Manager;

import com.example.demo.config.auth.JsonCustomLogin.Service.CustomLoginService;
import com.example.demo.config.auth.Oauth.CustomOAuth2User;
import com.example.demo.config.auth.jwt.UserData.CustomUserDetail;
import com.example.demo.domain.Customer;
import com.example.demo.persistence.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
// CustomAuthenticationManager에서 passwordEncoder, customLoginService를 통해 받아온 authentication이 정상적인 정보인지 확인을 한다.

public class CustomAuthenticationManager  implements AuthenticationManager {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomLoginService customLoginService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //customLoginService.loadUserByUsername 메소드를 통해 CustomUserDetail 객체를 가져온다.
        CustomUserDetail customUserDetail=customLoginService.loadUserByUsername(authentication.getPrincipal().toString());
        // CustomUserDetail 객체의 정보를 통해 Customer 객체를 가져온다.
        Customer customer=customUserDetail.getCustomer();

        //passwordEncoder로 db에 가져온 passwd와 입력한 passwd와 같은지 비교
        if (passwordEncoder.matches(authentication.getCredentials().toString(), customer.getUserPasswd())) {
            //해당 유저의 권한 리스트를 받아올 GrantedAuthority 객체 리스트를 생성
            List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
            //로그인한 고객의 역할(role)를 객체 리스트에 삽입
            grantedAuthorityList.add(new SimpleGrantedAuthority((customer.getRole().getKey())));
            // 기존 principal, credentials 이 존재하는 UsernamePasswordAuthenticationToken에
            // role를 추가한 새로운 UsernamePasswordAuthenticationToken에를 반환
            return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), grantedAuthorityList);
        } else throw new BadCredentialsException("비밀번호가 틀렸습니다.");
    }
}
