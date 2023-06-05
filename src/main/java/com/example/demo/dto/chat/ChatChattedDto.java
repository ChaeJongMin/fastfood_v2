package com.example.demo.dto.chat;

import com.example.demo.domain.Chat;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
@Getter
public class ChatChattedDto {
    private String userId;
    private String message;
    private String sendTime;

    public ChatChattedDto(Chat chat){
        this.userId=chat.getSender().getUserId();
        this.message=chat.getMessage();
        this.sendTime= chat.getCreateDates().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
    }
}
