package com.example.demo.config.auth.jwt.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class HttpResponseDto {
    private String msg;
    private int statusCode;

    public HttpResponseDto(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }

}
