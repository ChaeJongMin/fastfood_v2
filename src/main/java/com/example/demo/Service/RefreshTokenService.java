package com.example.demo.Service;

import com.example.demo.config.auth.jwt.JwtTokenProvider;
import com.example.demo.config.auth.jwt.domain.RefreshTokens;
import com.example.demo.config.auth.jwt.domain.Util.TokenUtils;
import com.example.demo.dto.TokenDto;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.TokenCheckException;
import com.example.demo.exception.TokenNotFoundException;
import com.example.demo.exception.message.ExceptionMessage;
import com.example.demo.persistence.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
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
        return refreshTokenRepository.save(RefreshTokens.from(userId,jwtTokenProvider.refreshGenerateToken(userId,expirationTime),expirationTime));
    }
    @Transactional(readOnly = true)
    public RefreshTokens findTokenById(String userId){
        return refreshTokenRepository.findByKeyId(userId).orElseThrow(()->new TokenNotFoundException(ExceptionMessage.TOKEN_NOT_FOUND));
    }
    @Transactional
    public void delete(String userId){
        refreshTokenRepository.deleteByKeyId(userId);
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
    public boolean checkExpireTime(String refreshToken){
        if(!refreshToken.equals("not exist") && jwtTokenProvider.validateRefreshToken(refreshToken))
            return true;
        return false;
    }
    @Transactional(readOnly = true)
    public TokenDto reissue(String refreshToken) {
        Optional<RefreshTokens> refreshTokenObject=refreshTokenRepository.findByValue(refreshToken);
        if(refreshTokenObject==null || !jwtTokenProvider.validateRefreshToken(refreshToken)) {
            //에러 발생
            return null;
        }
        log.info("해당 리프레쉬 토큰은 유효");
        return createRefreshToken(refreshToken,refreshTokenObject.get().getKeyId());
    }
    @Transactional
    public TokenDto createRefreshToken(String refreshToken, String userId) {
        String accessToken=jwtTokenProvider.generateToken(userId,JwtTokenProvider.ACCESS_TIME);
        log.info("새롭게 만든 토큰: "+accessToken);
        return TokenDto.createTokenDto(accessToken);
    }

}
