package com.example.demo.Controller.Error;

import com.example.demo.exception.CustomException;
import com.example.demo.exception.ErrorResponse;
import com.example.demo.exception.GlobalException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = { GlobalException.class })
    protected ResponseEntity<?> handleCustomException(GlobalException e) throws JsonProcessingException {
        log.error("handleCustomException throw CustomException : {}", e.getRefreshErrorCode());
        return ErrorResponse.toResponseEntity(e.getRefreshErrorCode());
    }
}
//
