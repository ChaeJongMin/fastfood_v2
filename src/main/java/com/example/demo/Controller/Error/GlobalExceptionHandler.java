package com.example.demo.Controller.Error;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    // 서버 오류
    @ExceptionHandler({ Exception.class })
    protected String handleServerException(Exception ex) {
        log.info("handleServerException: "+ex);
        return "error/error500";
    }
}
