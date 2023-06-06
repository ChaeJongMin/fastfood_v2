package com.example.demo.exception.Exception;

import com.example.demo.exception.errorCode.ControllerErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ControllerException extends RuntimeException {
    private final ControllerErrorCode errorCode;
}
