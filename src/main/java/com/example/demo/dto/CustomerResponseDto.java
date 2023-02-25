package com.example.demo.dto;

import com.example.demo.domain.Customer;
import lombok.Getter;

@Getter
public class CustomerResponseDto {
    private String userId;
    private String userPasswd;
    private String email;
    private String cardNum;
    private String cardCompany;
    private String phoneNum;

    public CustomerResponseDto(Customer customerEntity){
        this.userId=customerEntity.getUserId();
        this.userPasswd= customerEntity.getUserPasswd();
        this.email= customerEntity.getEmail();
        this.cardNum= customerEntity.getCardNum();
        this.cardCompany= customerEntity.getCardCompany();
        this.phoneNum= customerEntity.getPhoneNum();
    }
}
