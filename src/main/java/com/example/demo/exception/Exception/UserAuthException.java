package com.example.demo.exception.Exception;

import com.example.demo.exception.message.ExceptionMessage;

public class UserAuthException extends RuntimeException {
    public UserAuthException(String error) {
        super(error);
    }

    public UserAuthException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.message());
    }
}
