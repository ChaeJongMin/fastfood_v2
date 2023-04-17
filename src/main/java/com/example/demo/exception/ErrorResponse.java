package com.example.demo.exception;

import com.example.demo.exception.errorCode.ErrorCode;
import com.example.demo.exception.errorCode.RefreshErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;

    public static ResponseEntity<String> toResponseEntity(RefreshErrorCode errorCode) throws JsonProcessingException {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(new ObjectMapper().writeValueAsString(
                        ErrorResponse.builder()
                        .status(errorCode.getHttpStatus().value())
                        .error(errorCode.getHttpStatus().name())
                        .code(errorCode.name())
                        .message(errorCode.getDetail())
                        .build()
                ));
    }

}
