package com.example.demo.dto.Request;

import lombok.Getter;


// 비밀번호를 업데이트할 정보를 담은 DTO

@Getter
public class MailUpdatedDto {
    String email;
    String passwd;
}
