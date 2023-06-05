package com.example.demo.dto.chat;

import com.example.demo.domain.Customer;
import lombok.Getter;

@Getter
public class ChatFindUserResponseDto {
    private int uid;
    private String userName;
    private boolean isCheck;
    public ChatFindUserResponseDto(Customer customer,boolean isCheck){
        this.uid=customer.getId();
        this.userName= customer.getUserId();
        this.isCheck=isCheck;
    }
}
