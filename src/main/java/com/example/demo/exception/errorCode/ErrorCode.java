package com.example.demo.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    UNKNOWN_ERROR(UNAUTHORIZED, "알 수 없는 에러"),
    ACCESS_TOKEN_EXPIRED(UNAUTHORIZED, "액세스 토큰이 만료되었습니다."),
    FAIL_TOKEN_CHECK(UNAUTHORIZED,"토큰 검증에 실패했습니다."),
    MISMATCH_USERNAME_TOKEN(UNAUTHORIZED,"유저명이 토큰과 맞지 않습니다."),
    AUTHORIZED_DENIED(UNAUTHORIZED,"인증이 실패했습니다."),

    INVALID_AUTH_TOKEN(FORBIDDEN, "권한 정보가 없는 토큰입니다"),

    MEMBER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "로그아웃 된 사용자입니다"),
    TIMELIMIT_REFRESH_TOKEN(UNAUTHORIZED, "리프레시 토큰이 만료되었습니다."),
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "리프레시 토큰이 유효하지 않습니다"),
    MISMATCH_REFRESH_TOKEN(UNAUTHORIZED, "리프레시 토큰의 유저 정보가 일치하지 않습니다"),
    EMPTY_REFRESH_TOKEN(UNAUTHORIZED, "리프레시 토큰이 비어있습니다."),
    UNSUPPORT_REFRESH_TOKEN(UNAUTHORIZED, "리프레시 토큰을 지원하지 않습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String detail;
}