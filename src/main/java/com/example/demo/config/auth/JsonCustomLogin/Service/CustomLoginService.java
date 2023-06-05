package com.example.demo.config.auth.JsonCustomLogin.Service;

import com.example.demo.config.auth.jwt.UserData.CustomUserDetail;
import com.example.demo.domain.Customer;
import com.example.demo.exception.TokenCheckException;
import com.example.demo.exception.message.ExceptionMessage;
import com.example.demo.persistence.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CustomLoginService implements UserDetailsService {
    private final CustomerRepository customerRepository;
    @Override
    public CustomUserDetail loadUserByUsername(String userId) throws BadCredentialsException {
        return new CustomUserDetail(customerRepository.findByUserId(userId).get(0));
    }
}
