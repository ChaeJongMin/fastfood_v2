package com.example.demo.config.auth.jwt.domain.Util;

import com.example.demo.config.auth.jwt.JwtTokenProvider;
import com.example.demo.config.auth.jwt.UserData.CustomLoadUserByUsername;
import com.example.demo.config.auth.jwt.UserData.CustomUserDetail;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.TokenCheckException;
import com.example.demo.exception.errorCode.ErrorCode;
import com.example.demo.exception.message.ExceptionMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static com.example.demo.exception.errorCode.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class TokenUtils {
    private Logger logger = LogManager.getLogger(this.getClass());
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomLoadUserByUsername customLoadUserByUsername;
    public boolean vaildateToken(String token) throws JsonProcessingException {
        if(!StringUtils.hasText(token))
            throw new CustomException(FAIL_TOKEN_CHECK);
        else if(this.jwtTokenProvider.parseClaim(token).getExpiration().before(new Date())){
            throw new CustomException(ACCESS_TOKEN_EXPIRED);
        }
        System.out.println("** vaildateToken 메소드 종료 **");
        return true;
    }
    public Authentication getAuthentication(String token){
        String userName = this.jwtTokenProvider.parseToken(token);
        if(userName == null){
            throw new CustomException(MISMATCH_USERNAME_TOKEN);
        }
        UserDetails userDetails=customLoadUserByUsername.loadUserByUsername(userName);
        if (!userDetails.getUsername().equals(userName)) {
            throw new CustomException(MISMATCH_USERNAME_TOKEN);
        }
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
