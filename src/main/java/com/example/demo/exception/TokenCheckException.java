package com.example.demo.exception;

import com.example.demo.exception.message.ExceptionMessage;

public class TokenCheckException  extends RuntimeException {
    public TokenCheckException(String error) {
        super(error);
    }

    public TokenCheckException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.message());
    }
}
