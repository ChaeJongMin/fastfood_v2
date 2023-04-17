package com.example.demo.dto;

import com.example.demo.config.auth.jwt.JwtTokenProvider;
import lombok.Builder;
import lombok.Getter;
@Builder
@Getter
public class TokenDto {
    private String grantType;
    private String accessToken;


    public static TokenDto createTokenDto(String accessToken){
        return TokenDto.builder()
                .grantType(JwtTokenProvider.TOKEN_PREFIX)
                .accessToken(accessToken)
                .build();
    }
}
