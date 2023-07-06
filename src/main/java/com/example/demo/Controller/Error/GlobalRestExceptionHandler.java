package com.example.demo.Controller.Error;

import com.example.demo.dto.error.ErrorResponse;
import com.example.demo.exception.Exception.ControllerException;
import com.example.demo.exception.Exception.DuplicateException;
import com.example.demo.exception.errorCode.ControllerErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Slf4j
@RestControllerAdvice
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {
    // 컨트롤러에 발생하는 에러
    // ControllerException 예외를 처리하는 데 사용할 수 있음을 지시
    @ExceptionHandler({ ControllerException.class })
    protected ResponseEntity handleControllerException(ControllerException ex) {
        //발생한 예외에서 errorCode를 추출
        ControllerErrorCode errorCode=ex.getErrorCode();
        //응답에 에러 상세 내용 전달
        return new ResponseEntity(new ErrorResponse(errorCode), HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }
    // 중복 에러
    @ExceptionHandler({ DuplicateException.class })
    protected ResponseEntity handleDuplicateException(DuplicateException ex) {
        ControllerErrorCode errorCode=ex.getErrorCode();
        return new ResponseEntity(new ErrorResponse(errorCode), HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }



    // 서버 오류
//    @ExceptionHandler({ Exception.class })
//    protected ResponseEntity handleServerException(Exception ex) {
////        return new ResponseEntity(new ErrorResponse(ControllerErrorCode.INTER_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
//        log.info("handleServerException: "+ex);
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ModelAndView("/error/error500")
//                .getViewName());
//    }


}

