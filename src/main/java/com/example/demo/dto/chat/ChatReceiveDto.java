package com.example.demo.dto.chat;

import com.example.demo.domain.Chat;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
@Getter
//수신할 chat에 대한 정보
public class ChatReceiveDto {
    //채팅방 아이디
    private Long roomId;
    //수신할 유저아이디
    private String receiverUser;
    //송신한 유저아이디
    private String senderUser;
    //보낼 메시지
    private String message;
    //보낸 시간
    private String sendTime;

    public ChatReceiveDto (Chat chat){
        this.roomId=chat.getId();
        this.receiverUser=chat.getReceiver().getUserId();
        this.senderUser=chat.getSender().getUserId();
        this.message=chat.getMessage();
        this.sendTime=chat.getCreateDates().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm"));
    }

}

