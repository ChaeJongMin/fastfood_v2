package com.example.demo.dto.Request;

import lombok.Getter;
import lombok.Setter;

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
