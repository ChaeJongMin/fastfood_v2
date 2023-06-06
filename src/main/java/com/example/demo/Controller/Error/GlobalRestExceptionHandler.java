package com.example.demo.Controller.Error;

import com.example.demo.dto.error.ErrorResponse;
import com.example.demo.exception.Exception.ControllerException;
import com.example.demo.exception.Exception.DuplicateException;
import com.example.demo.exception.errorCode.ControllerErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Slf4j
@RestControllerAdvice
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {
    // 중복 관련 예외 처리 (아이디, 비밀번호)
    @ExceptionHandler({ ControllerException.class })
    protected ResponseEntity handleDuplicateException(ControllerException ex) {
        ControllerErrorCode errorCode=ex.getErrorCode();
        return new ResponseEntity(new ErrorResponse(errorCode), HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }
    @ExceptionHandler({ DuplicateException.class })
    protected ResponseEntity handleControllerException(DuplicateException ex) {
        ControllerErrorCode errorCode=ex.getErrorCode();
        return new ResponseEntity(new ErrorResponse(errorCode), HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }
    // 서버 오류
    @ExceptionHandler({ Exception.class })
    protected ResponseEntity handleServerException(Exception ex) {
        return new ResponseEntity(new ErrorResponse(ControllerErrorCode.INTER_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}

