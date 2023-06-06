package com.example.demo.config.auth.JsonCustomLogin.Service;

import com.example.demo.config.auth.jwt.UserData.CustomUserDetail;
import com.example.demo.domain.Customer;
import com.example.demo.exception.TokenCheckException;
import com.example.demo.exception.message.ExceptionMessage;
import com.example.demo.persistence.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class CustomLoginService implements UserDetailsService {
    private final CustomerRepository customerRepository;
    //customer 테이블에 접근하여 매개변수인 userId로 CustomerDetail 객체를 반환
    @Override
    public CustomUserDetail loadUserByUsername(String userId) throws BadCredentialsException {
        log.info("loadUserByUsername 실행");

        List<Customer> customers = customerRepository.findByUserId(userId);

        if (customers.isEmpty()) {
            throw new  UsernameNotFoundException("해당 유저는 없습니다.");
        }

        return new CustomUserDetail(customers.get(0));
    }
}
