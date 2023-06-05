package com.example.demo.dto.Request;

import com.example.demo.domain.Customer;
import com.example.demo.domain.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@NoArgsConstructor
public class CustomerRequestDto {
    private String userId;
    private String userPasswd;
    private String email;
    private String cardNum;
    private String cardCompany;
    private String phoneNum;
    private Integer role=0;

    @Builder
    public CustomerRequestDto(String userId,String userPasswd,String email
    ,String cardNum,String cardCompany,String phoneNum,int role){
        this.userId=userId;
        this.userPasswd=userPasswd;
    }
    public Customer toCustomerEntitiy(){
        return Customer.builder()
                .userId(userId)
                .userPasswd(userPasswd)
                .email(email)
                .cardNum(cardNum)
                .cardCompany(cardCompany)
                .phoneNum(phoneNum)
                .role(Role.USER)
                .boardList(new ArrayList<>())
                .build();
    }
}
