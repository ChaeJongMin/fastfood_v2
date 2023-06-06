package com.example.demo.exception.Exception;

import com.example.demo.exception.errorCode.ControllerErrorCode;
import com.example.demo.exception.errorCode.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DuplicateException extends RuntimeException{
    private final ControllerErrorCode errorCode;
}
