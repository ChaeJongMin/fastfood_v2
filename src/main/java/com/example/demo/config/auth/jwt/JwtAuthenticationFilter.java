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
        //필터에서 액세스 토큰 검사가 필요없는 uri 배
        private static final String[] ALL_WHITELIST = {
            "/api/auth/reissue",  "/api/auth/delete",
            "/api/customer" , "/api/customer/login", "/api/customer", 
            "/fastfood/login", "/fastfood/ResetPasswd", "/fastfood/register",  
            "/favicon.ico"               
        };

        /*************************************************************************************************************/
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {            
           // 액세스 토큰 검사를 할 필요 없는 uri일 시 그냥 필터 통과 
          if(isFilterCheck(request.getRequestURI())){                    
                filterChain.doFilter(request,response);
                return ;
            }
            // 액세스 토큰 검사가 필요한 uri
            else {
                //쿠키에 존재하는 액세스 토큰을 가져온다.    
                String token = this.resolveAccessTokenFromRequest(request);
                // 액세스 토큰 유효성 검사
                // 토큰이 비어있지 않고 정상적이 토큰일 시    
                if (token != null && this.tokenUtils.vaildateToken(token)) {
                    // 유효한 토큰을 통해 인증된 유저의 정보를 받아온다.    
                    Authentication authentication = this.tokenUtils.getAuthentication(token);
                    // 그 정보를 SecurityContextHolder 저장    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                //다음 필터 진행
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
//            log.info("가져온 토큰: "+accessToken);
            //액세스 토큰의 value를     
            return jwtTokenProvider.resolveToken(accessToken);
        }
        private boolean isFilterCheck(String requestURI){
            if (requestURI.startsWith("/api/mail") || requestURI.startsWith("/oauth2") || requestURI.startsWith("/login") ) {
                return true;
            }
          for(String excludeURi : ALL_WHITELIST){
              if(excludeURi.equals(requestURI))
                  return true;
          }
          if(requestURI.matches(".*(css|jpg|png|gif|js|ico)")){
              return true;
          }
          return false;
        }
// private boolean isFilterCheck(String requestURI) {
//   return requestURI.startsWith("/api/mail")
//       || requestURI.startsWith("/oauth2")
//       || requestURI.startsWith("/login")
//       || Arrays.asList(ALL_WHITELIST).contains(requestURI)
//       || requestURI.matches(".*(css|jpg|png|gif|js|ico)");
// }

        /*************************************************************************************************************/



}
