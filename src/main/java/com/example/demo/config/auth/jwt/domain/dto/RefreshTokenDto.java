package com.example.demo.config.auth.jwt.domain.dto;

import com.example.demo.config.auth.jwt.domain.RefreshTokens;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RefreshTokenDto {
    private String value;
    private Long expiration;

    public RefreshTokenDto(RefreshTokens refreshTokens){
        this.value=refreshTokens.getValue();
        this.expiration=refreshTokens.getExpiration();
    }
}
