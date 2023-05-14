package com.example.demo.config.auth.jwt;

import com.example.demo.exception.CustomException;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response); // go to 'JwtAuthenticationFilter'
        } catch (CustomException e) {
            log.info(request.getRequestURI().toString()+" : "+e.getErrorCode().name());
            setErrorResponse(response,e);
            response.setStatus(e.getErrorCode().getHttpStatus().value());
            response.setContentType("application/json;charset=UTF-8");
            JSONObject responseJson = new JSONObject();
            responseJson.put("message", e.getErrorCode().getDetail());
            responseJson.put("code", e.getErrorCode().name());
            response.getWriter().print(responseJson);
        }
    }
    public void setErrorResponse(HttpServletResponse res, CustomException e) throws IOException {
        res.setStatus(e.getErrorCode().getHttpStatus().value());
        res.setContentType("application/json; charset=UTF-8");
        JSONObject responseJson = new JSONObject();
        responseJson.put("message", e.getErrorCode().getDetail());
        responseJson.put("code", e.getErrorCode().name());
        res.getWriter().print(responseJson);
    }
}