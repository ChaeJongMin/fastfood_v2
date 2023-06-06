package com.example.demo.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ControllerErrorCode {
    NOT_FOUND_USER(404,"MEMBER-ERR-404","해당 유저는 존재하지 않습니다."),


    INTER_SERVER_ERROR(500,"COMMON-ERR-500","INTER SERVER ERROR"),

    EMAIL_DUPLICATION(409,"MEMBER-ERR-400","중복되는 이메일이 있어요."),
    USERID_DUPLICATION(409,"MEMBER-ERR-400","중복되는 유저 아이디가 있어요.");



    private int status;
    private String errorCode;
    private String message;
}
