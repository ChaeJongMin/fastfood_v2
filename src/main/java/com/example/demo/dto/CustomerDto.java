package com.example.demo.dto;

import com.example.demo.domain.Customer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDto {
    private Integer id;
    private String userId; //PK
    private String userPasswd;
    private String email;
    private String cardNum;
    private String cardCompany;
    private String phoneNum;
    private Integer role=0;

    public CustomerDto(Customer customerEntity){
        this.id=customerEntity.getId();
        this.userId=customerEntity.getUserId();
        this.userPasswd= customerEntity.getUserPasswd();
        this.email= customerEntity.getEmail();
        this.cardNum= customerEntity.getCardNum();
        this.cardCompany= customerEntity.getCardCompany();
        this.phoneNum= customerEntity.getPhoneNum();
        this.role= customerEntity.getRole();
    }

}
