package com.example.demo.dto.error;

import com.example.demo.exception.errorCode.ControllerErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private int status;
    private String message;
    private String code;

    public ErrorResponse(ControllerErrorCode errorCode){
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
        this.code = errorCode.getErrorCode();
    }
}
