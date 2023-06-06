package com.example.demo.config.auth.jwt.UserData;

import com.example.demo.domain.Customer;
import com.example.demo.exception.Exception.TokenCheckException;
import com.example.demo.exception.message.ExceptionMessage;
import com.example.demo.persistence.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
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
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        System.out.println("--------------------------loadUserByUsername--------------------------");
        List<Customer> customer = customerRepository.findByUserId(userId);
        if(customer==null || customer.isEmpty()){
            throw new TokenCheckException(ExceptionMessage.USER_NOT_FOUND);
        }
        CustomUserDetail customUserDetail=new CustomUserDetail(customer.get(0));
        customUserDetail.setCustomer(customer.get(0));
        customUserDetail.setId(customer.get(0).getId());
        return customUserDetail;
    }

}