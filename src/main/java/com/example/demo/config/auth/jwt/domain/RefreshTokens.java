package com.example.demo.config.auth.jwt.domain;

import com.example.demo.domain.BaseTime;
import lombok.*;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_token")
@Entity
@Builder
public class RefreshTokens extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String keyId;

    //refresh 토큰 값
    @Column(nullable = false)
    private String value;

    private Long expiration;
    @Builder
    public RefreshTokens(String keyId, String value) {
        this.keyId = keyId;
        this.value = value;
    }
    public static RefreshTokens from(String keyId, String value, Long expiration){
        return RefreshTokens.builder()
                .keyId(keyId)
                .value(value)
                .expiration(expiration/1000)
                .build();
    }
    public void update(String value, Long expiration){
        this.value=value;
        this.expiration=expiration/1000;
    }


}