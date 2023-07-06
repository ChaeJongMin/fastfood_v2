package com.example.demo.dto.Request;

import lombok.Getter;
import lombok.Setter;


//이메일과 아이디를 체크할 정보를 담은 DTO

@Getter
@Setter
public class MailCheckDto {
    String email;
    String name;

    public MailCheckDto(String name, String email){
        this.name=name;
        this.email=email;
    }
}
