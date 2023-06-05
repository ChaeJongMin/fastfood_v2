package com.example.demo.config.auth.JsonCustomLogin;

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
public class CustomAuthenticationManager  implements AuthenticationManager {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info(authentication.getPrincipal().toString());
        List<Customer> customerList=customerRepository.findByUserId(authentication.getPrincipal().toString());
        if(customerList!=null && !customerList.isEmpty()){
            Customer customer=customerList.get(0);
            if(passwordEncoder.matches(authentication.getCredentials().toString(), customer.getUserPasswd())){
                List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
                grantedAuthorityList.add(new SimpleGrantedAuthority((customer.getRole().getKey())));
                return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), grantedAuthorityList);
            } else  throw new BadCredentialsException("비밀번호가 틀렸습니다.");

        } else{
            throw new UsernameNotFoundException("해당 유저는 없습니다.");
        }

    }


}
