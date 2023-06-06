package com.example.demo.exception.Exception;

import com.example.demo.exception.errorCode.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
}