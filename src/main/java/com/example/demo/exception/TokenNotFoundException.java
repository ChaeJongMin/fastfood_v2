package com.example.demo.exception;

import com.example.demo.exception.message.ExceptionMessage;

public class TokenNotFoundException extends RuntimeException{
    public TokenNotFoundException(String error) {
        super(error);
    }

    public TokenNotFoundException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.message());
    }
}
