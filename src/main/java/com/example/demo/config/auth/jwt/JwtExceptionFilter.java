package com.example.demo.config.auth.jwt;

import com.example.demo.exception.Exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {
    // jwt 토큰을 유효성 검사시 발생되는 예외를 처리하는 필터 
    // 예외가 발생 시 발생한 예외 메시지와 코드를 통해 클라이언트에 전달
    // 클라이언트가 예외 정보를 받아 알맞게 처
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            //다음 필터인 JwtAuthenticationFilter 진행
            filterChain.doFilter(request, response); // go to 'JwtAuthenticationFilter'
        } catch (CustomException e) { //예외가 발생 시
            //클라이언트에 응답할 예외 메시지 및 코드를 생성 후 전달
            log.info(request.getRequestURI().toString()+" : "+e.getErrorCode().name());
            setErrorResponse(response,e);
//            response.setStatus(e.getErrorCode().getHttpStatus().value());
//            response.setContentType("application/json;charset=UTF-8");
//            JSONObject responseJson = new JSONObject();
//            responseJson.put("message", e.getErrorCode().getDetail());
//            responseJson.put("code", e.getErrorCode().name());
//            response.getWriter().print(responseJson);

           //아래 코드는 필요없다 지워도 될듯
            response.setStatus(e.getErrorCode().getHttpStatus().value());
            response.setContentType("application/json;charset=UTF-8");
           
            JSONObject responseJson = new JSONObject();
            responseJson.put("message", e.getErrorCode().getDetail());
            responseJson.put("code", e.getErrorCode().name());
            response.getWriter().print(responseJson);
        }
    }
    //실질적으로 예외 메시지 및 코드를 생성 후 전달하는 메소드
    public void setErrorResponse(HttpServletResponse res, CustomException e) throws IOException {
        res.setStatus(e.getErrorCode().getHttpStatus().value());
        res.setContentType("application/json; charset=UTF-8");
        JSONObject responseJson = new JSONObject();
        responseJson.put("message", e.getErrorCode().getDetail());
        responseJson.put("code", e.getErrorCode().name());
        res.getWriter().print(responseJson);
    }
}
