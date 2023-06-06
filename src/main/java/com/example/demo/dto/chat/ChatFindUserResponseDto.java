package com.example.demo.dto.chat;

import com.example.demo.domain.Customer;
import lombok.Getter;

@Getter
//찾아진 유저 정보를 담고있는 dto
public class ChatFindUserResponseDto {
    //유저 아이디(기본키)
    private int uid;
    //유저 이름
    private String userName;
    //대화 유저 여부
    //해당 유저가 이미 사용자와 대화방이 존재하는 지를 true/false를 나타내고 있다.
    private boolean isCheck;
    public ChatFindUserResponseDto(Customer customer,boolean isCheck){
        this.uid=customer.getId();
        this.userName= customer.getUserId();
        this.isCheck=isCheck;
    }
}
