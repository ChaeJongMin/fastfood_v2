package com.example.demo.dto.chat;

import com.example.demo.domain.Chat;
import lombok.Getter;

@Getter
//기존에 대화한 유저의 정보를 담고있는 dto
public class ChatUserDto {
    //방번호
    private Long roomId;
    //상대방 유저아이디
    private String opponentName;
    //대화했던 가장 최근 메시지
    private String lastMessage;

    public ChatUserDto (Chat chat,String myUserId){
        this.roomId=chat.getId();
        this.opponentName=getOpponent(chat.getSender().getUserId(), chat.getReceiver().getUserId(), myUserId);
        this.lastMessage=chat.getMessage();
    }
    // 상대방 이름 파악 메소드
    // 가장 최근 메시지가 상대방이 보낼 수도 있고 자신이 보낼 수 있기 떄문에 별도의 메소드로 상대방 유저아이디 판별
    public String getOpponent(String senderUserId,String receiverUserId,String myUserId){
        //수신될 유저아이디가 사용자 아이디와 같을 시
        if(receiverUserId.equals(myUserId)){
            //송신자 유저아이드를 전달
            return senderUserId;
        }
        return receiverUserId;
    }
}
