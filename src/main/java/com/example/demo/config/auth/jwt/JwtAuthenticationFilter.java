package com.example.demo.config.auth.jwt;

import com.example.demo.config.auth.jwt.domain.Util.TokenUtils;

import com.example.demo.exception.errorCode.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.persistence.Column;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
        private final JwtTokenProvider jwtTokenProvider;
        private final TokenUtils tokenUtils;
        private static final String[] ALL_WHITELIST = {
            "/api/auth/reissue", "/fastfood/api/auth/reissue",
            "/api/customer/logout", "/fastfood/api/customer/logout",
            "/fastfood/login", "/api/auth/delete", "/favicon.ico","/fastfood/register","/login","/api/customer/login"
        };

        /*************************************************************************************************************/
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            //requestValue를 통해 재발급 api이면
            //filterChain.doFilter(request,response); 작동
//            log.info(request.getRequestURI().toString()+" 호출 !!");
            if(isFilterCheck(request.getRequestURI())){
//                log.info(request.getRequestURI()+" : "+"인증필터가 무시됨!!!");
                filterChain.doFilter(request,response);
                return ;
            }
            else {
//                log.info(request.getRequestURI() + " : " + "액세스토큰 검사!!!");
                String token = this.resolveAccessTokenFromRequest(request);
//                log.info(token);
                if (token != null && this.tokenUtils.vaildateToken(token)) {
                    Authentication authentication = this.tokenUtils.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                filterChain.doFilter(request, response);
            }
        }
        //헤더에 있는 액세스 토큰을 추출
        private String resolveAccessTokenFromRequest(HttpServletRequest request) {
            String accessToken= Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals("accessToken"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
            log.info("가져온 토큰: "+accessToken);
            return jwtTokenProvider.resolveToken(accessToken);
        }
        private boolean isFilterCheck(String requestURI){
          for(String excludeURi : ALL_WHITELIST){
              if(excludeURi.equals(requestURI))
                  return true;
          }
          if(requestURI.matches(".*(css|jpg|png|gif|js|ico)")){
              return true;
          }
          return false;
        }

        /*************************************************************************************************************/



}