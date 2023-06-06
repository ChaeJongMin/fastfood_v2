package com.example.demo.dto.chat;

import com.example.demo.domain.Chat;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
@Getter
//이전 대화 내용을 저장하는 dto입니다.
public class ChatChattedDto {
    //메시지를 보낸 유저아이디
    private String userId;
    //보낸 메시지
    private String message;
    //보낸 시간
    private String sendTime;

    public ChatChattedDto(Chat chat){
        this.userId=chat.getSender().getUserId();
        this.message=chat.getMessage();
        this.sendTime= chat.getCreateDates().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
    }
}
