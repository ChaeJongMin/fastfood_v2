package com.example.demo.dto.chat;

import com.example.demo.domain.Chat;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
@Getter
public class ChatReceiveDto {
    private Long roomId;
    private String receiverUser;
    private String senderUser;
    private String message;
    private String sendTime;

    public ChatReceiveDto (Chat chat){
        this.roomId=chat.getId();
        this.receiverUser=chat.getReceiver().getUserId();
        this.senderUser=chat.getSender().getUserId();
        this.message=chat.getMessage();
        this.sendTime=chat.getCreateDates().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm"));
    }

}

