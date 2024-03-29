package com.example.demo.Service;

import com.example.demo.config.auth.jwt.JwtTokenProvider;
import com.example.demo.config.auth.jwt.domain.RefreshTokens;
import com.example.demo.config.auth.jwt.domain.Util.TokenUtils;
import com.example.demo.config.auth.jwt.domain.dto.RefreshTokenDto;
import com.example.demo.dto.Response.TokenDto;
import com.example.demo.exception.Exception.TokenNotFoundException;
import com.example.demo.exception.message.ExceptionMessage;
import com.example.demo.persistence.RefreshTokenRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenUtils tokenUtils;
    @Transactional
    public RefreshTokens save(String userId, long expirationTime){
        String value=jwtTokenProvider.refreshGenerateToken(userId,expirationTime);
        long expireTime=jwtTokenProvider.getExpiredTime(value);

        Optional<RefreshTokens> refreshTokens=refreshTokenRepository.findByKeyId(userId);

        if(refreshTokens.isPresent()){
            update(userId,value,expireTime);
        } else{
            return refreshTokenRepository.save(RefreshTokens.from(userId,value,expireTime));
        }
        return refreshTokens.get();
    }

    @Transactional(readOnly = true)
    public RefreshTokenDto findTokenById(String userId){
        RefreshTokens refreshTokens=refreshTokenRepository.findByKeyId(userId).orElseThrow(()->new TokenNotFoundException(ExceptionMessage.TOKEN_NOT_FOUND));
        return new RefreshTokenDto(refreshTokens);
    }
    @Transactional
    public void delete(String userId){
        refreshTokenRepository.deleteByKeyId(userId);
    }

    @Transactional
    public String update(String userId,String value,long time){
        //업데이트 다시 수정
        RefreshTokens refreshTokens=refreshTokenRepository.findByKeyId(userId).orElseThrow(()->new TokenNotFoundException(ExceptionMessage.TOKEN_NOT_FOUND));
        refreshTokens.update(value,time);
        return refreshTokens.getValue();
    }

    @Transactional
    public void deleteByRefreshToken(String tokenValue){
        refreshTokenRepository.deleteByValue(tokenValue);
    }
    @Transactional(readOnly = true)
    public boolean findRefreshTokenbyUser(String userId){
        return refreshTokenRepository.existsByKeyId(userId);
    }

    @Transactional(readOnly = true)
    public boolean checkExpireTime(long expiredTime){
        long nowTime=new Date().getTime()/1000;
        if(expiredTime<nowTime)
            return false;
        return true;
    }
    @Transactional(readOnly = true)
    public TokenDto reissue(String refreshToken) throws JsonProcessingException {
        Optional<RefreshTokens> refreshTokenObject=refreshTokenRepository.findByValue(refreshToken);
        if(refreshTokenObject!=null && jwtTokenProvider.vaildateRefreshToken(refreshToken)) {
            //이미 전역 예외 컨트롤러에서 예외처리가 발생하면 처리
            return createAccessToken(refreshToken,refreshTokenObject.get().getKeyId());

        }
        log.info("해당 리프레쉬 토큰은 유효");
        return null;
    }
    @Transactional
    public TokenDto createAccessToken(String refreshToken, String userId) {
        String accessToken=jwtTokenProvider.generateToken(userId,JwtTokenProvider.ACCESS_TIME);
        log.info("새롭게 만든 토큰: "+accessToken);
        return TokenDto.createTokenDto(accessToken);
    }
    @Transactional(readOnly = true)
    public boolean checkValue(String value){
        return refreshTokenRepository.existsByValue(value);
    }
}
