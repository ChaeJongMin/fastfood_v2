package com.example.demo.config.auth.dto;

import com.example.demo.domain.Customer;
import lombok.Getter;

import java.io.Serializable;
@Getter
public class SessionUser implements Serializable {
    private Integer id;
    private String userId;
    public SessionUser(Customer customer){
        System.out.println("SeesionUser 작동");
        this.id=customer.getId();
        this.userId=customer.getUserId();
    }
}
