package com.example.demo.exception.Exception;

import com.example.demo.exception.errorCode.ControllerErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
// 커스텀 예외 클래스
// RuntimeException 클래스를 확장하여 실행 시간 예외를 처리한다.
public class ControllerException extends RuntimeException {
    // ControllerErrorCode는 예외에 대한 고유한 에러 코드를 나타내는 열거형(enum) 타입
    // 에러의 상세 내용을 담고 있다.
    private final ControllerErrorCode errorCode;
}
