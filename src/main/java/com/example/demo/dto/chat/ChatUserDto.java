package com.example.demo.dto.chat;

import com.example.demo.domain.Chat;
import lombok.Getter;

@Getter
public class ChatUserDto {
    private Long roomId;
    private String opponentName;
    private String lastMessage;

    public ChatUserDto (Chat chat,String myUserId){
        this.roomId=chat.getId();
        this.opponentName=getOpponent(chat.getSender().getUserId(), chat.getReceiver().getUserId(), myUserId);
        this.lastMessage=chat.getMessage();
    }
    public String getOpponent(String senderUserId,String receiverUserId,String myUserId){
        if(receiverUserId.equals(myUserId)){
            return senderUserId;
        }
        return receiverUserId;
    }
}
