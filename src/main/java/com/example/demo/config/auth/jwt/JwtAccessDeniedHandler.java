package com.example.demo.config.auth.jwt;

import com.example.demo.exception.CustomException;
import com.example.demo.exception.errorCode.ErrorCode;
import org.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.demo.exception.errorCode.ErrorCode.INVALID_AUTH_TOKEN;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 필요한 권한이 없이 접근하려 할때 403

        throw new CustomException(INVALID_AUTH_TOKEN);
    }
    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());

        JSONObject responseJson = new JSONObject();
        responseJson.put("message", errorCode.getDetail());
        responseJson.put("code", errorCode.name());
        response.getWriter().print(responseJson);
    }
}