package com.example.demo.config.auth.jwt;


import com.example.demo.Service.RefreshTokenService;
import com.example.demo.dto.Response.TokenDto;
import com.example.demo.exception.Exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
    // jwt 토큰을 유효성 검사시 발생되는 예외를 처리하는 필터 
    // 예외가 발생 시 발생한 예외 메시지와 코드를 통해 클라이언트에 전달
    // 클라이언트가 예외 정보를 받아 알맞게 처
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            //다음 필터인 JwtAuthenticationFilter 진행
            filterChain.doFilter(request, response); // go to 'JwtAuthenticationFilter'
        } catch (CustomException e) { //예외가 발생 시
            //클라이언트에 응답할 예외 메시지 및 코드를 생성 후 전달
            log.info(request.getRequestURI().toString()+" : "+e.getErrorCode().name());
            if(e.getErrorCode().name().equals("ACCESS_TOKEN_EXPIRED") && request.getRequestURI().startsWith("/fastfood")){
                log.info("액세스 토큰 만료니 새롭게 발급");
                callReissueApi(response,request);
            }
            else
                setErrorResponse(response,e);

        }
    }
    //실질적으로 예외 메시지 및 코드를 생성 후 전달하는 메소드
    public void setErrorResponse(HttpServletResponse res, CustomException e) throws IOException {
//        if(e.getErrorCode().name().equals("ACCESS_TOKEN_EXPIRED")){
//            res.setStatus(200);
//        } else
            res.setStatus(e.getErrorCode().getHttpStatus().value());

        res.setContentType("application/json; charset=UTF-8");
        JSONObject responseJson = new JSONObject();
        responseJson.put("message", e.getErrorCode().getDetail());
        responseJson.put("code", e.getErrorCode().name());
        res.getWriter().print(responseJson);
    }
    public void callReissueApi(HttpServletResponse response,HttpServletRequest request) throws IOException {
        String refreshToken= Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("refreshToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
        //리프레쉬 토큰을 통해 액세스 토큰을 재발급한다.
        try{
            TokenDto accessTokenDto=refreshTokenService.reissue(refreshToken);
            jwtTokenProvider.setAccessTokenAtCookie(accessTokenDto.getAccessToken(), response);
            response.setStatus(200);
        }
        //재발급하는 과정에서 예외가 발생 시 지정한 에러페이지로 리다이렉트 한다.
        catch(CustomException e){
            response.setStatus(200);
            response.sendRedirect("/error/error401");
        }
    }
}
