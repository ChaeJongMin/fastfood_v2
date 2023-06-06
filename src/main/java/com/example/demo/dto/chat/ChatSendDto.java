package com.example.demo.dto.chat;

import lombok.Getter;

@Getter
//메시지를 송신한 chat 정보
public class ChatSendDto {
    //송신한 유저아이디
    private String senderNickname;
    //송신한 메시지
    private String message;
}
