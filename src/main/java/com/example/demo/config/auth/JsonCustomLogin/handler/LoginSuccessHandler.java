package com.example.demo.config.auth.JsonCustomLogin.handler;

import com.example.demo.Service.ConnectCustomerService;
import com.example.demo.Service.CustomerService;
import com.example.demo.Service.RefreshTokenService;
import com.example.demo.config.auth.jwt.JwtTokenProvider;
import com.example.demo.config.auth.jwt.UserData.CustomUserDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler  extends SimpleUrlAuthenticationSuccessHandler  {
    private final ConnectCustomerService connectCustomerService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("성공 핸들러 메소드 실행");
        String userId=authentication.getPrincipal().toString();
        String role=authentication.getAuthorities().toString();
        Set<String> roles = authentication.getAuthorities().stream()
                .map(r -> r.getAuthority()).collect(Collectors.toSet());

        connectCustomerService.increaseCurrentMonthCnt();
        String refreshValue=refreshTokenService.save(userId, JwtTokenProvider.REFRESH_TIME).getValue();
        jwtTokenProvider.setAccessTokenAndRefreshToken(jwtTokenProvider.generateToken(userId,jwtTokenProvider.ACCESS_TIME),
                refreshValue,response);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        HashMap<String, String> successMap=new HashMap<>();
        successMap.put("role",role);

        String result=objectMapper.writeValueAsString(successMap);

        response.getWriter().write(result);
//        response.sendRedirect("http://localhost:8080/fastfood/menu");

    }
}
