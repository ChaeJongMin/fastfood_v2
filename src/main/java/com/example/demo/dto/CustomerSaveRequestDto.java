package com.example.demo.dto;

import com.example.demo.domain.Customer;
import com.example.demo.domain.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;

@Getter
@NoArgsConstructor
public class CustomerSaveRequestDto {
    private String userId;
    private String userPasswd;
    private String email;
    private String cardNum;
    private String cardCompany;
    private String phoneNum;
    public void setEncorderPasswd(String convertPasswd){
        this.userPasswd=convertPasswd;
    }
    public Customer toCustomerEntitiy(){
        return Customer.builder()
                .userId(userId)
                .userPasswd(userPasswd)
                .email(email)
                .cardNum(cardNum)
                .cardCompany(cardCompany)
                .phoneNum(phoneNum)
                .boardList(new ArrayList<>())
                .commentList(new ArrayList<>())
                .role(Role.USER)
                .build();
    }
}
