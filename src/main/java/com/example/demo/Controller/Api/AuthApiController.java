package com.example.demo.Controller.Api;

import com.example.demo.Service.RefreshTokenService;
import com.example.demo.config.auth.jwt.JwtTokenProvider;
import com.example.demo.dto.Response.TokenDto;
import com.example.demo.exception.Exception.UserAuthException;
import com.example.demo.exception.message.ExceptionMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthApiController {
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    @PostMapping("/api/auth/authorize")
    public ResponseEntity authorize(@CookieValue("accessToken") String accessToken, Principal principal, HttpServletRequest request) {
        System.out.println("authorize 실행");
        if (principal == null || principal.getName() == null) {
            throw new UserAuthException(ExceptionMessage.NOT_AUTHORIZED_ACCESS);
        }
//        if (principal != null && principal.getName() != null) {
//            throw new UserAuthException(ExceptionMessage.NOT_AUTHORIZED_ACCESS);
//        }
        return new ResponseEntity(HttpStatus.OK);
    }
    @PostMapping("/api/auth/reissue")
    public ResponseEntity reissue(@CookieValue("refreshToken") String refreshToken, HttpServletResponse response) throws JsonProcessingException {
        log.info("reissue api 실행");
        log.info("가져온 리프레쉬 토큰: "+refreshToken);
        TokenDto accessTokenDto=refreshTokenService.reissue(refreshToken);
        jwtTokenProvider.setAccessTokenAtCookie(accessTokenDto.getAccessToken(), response);
        return new ResponseEntity(HttpStatus.OK);
    }
    @DeleteMapping("/api/auth/delete")
    public ResponseEntity<?> delete(@CookieValue("refreshToken") String refreshToken){
        refreshTokenService.deleteByRefreshToken(refreshToken);
        return ResponseEntity.ok("초기화 성공");
    }

}
