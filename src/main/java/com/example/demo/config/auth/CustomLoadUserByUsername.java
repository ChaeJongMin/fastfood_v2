package com.example.demo.config.auth;

import com.example.demo.config.auth.dto.SessionUser;
import com.example.demo.domain.Customer;
import com.example.demo.persistence.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSession;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomLoadUserByUsername{


    private final CustomerRepository customerRepository;
    private final HttpSession session;
    public Customer loadUserByUsername(String loginId) throws UsernameNotFoundException {
        List<Customer> customer = customerRepository.findByUserId(loginId);
        if(customer == null || customer.isEmpty())     throw new UsernameNotFoundException("Not Found User");
        session.setAttribute("user",new SessionUser(customer.get(0)));;
        return customer.get(0);
    }

}