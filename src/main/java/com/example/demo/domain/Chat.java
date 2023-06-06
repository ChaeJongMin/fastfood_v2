package com.example.demo.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Chat extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;
    //채팅방 객체
    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;
    //송신자(Customer객체)
    @ManyToOne
    private Customer sender;
    //수신자(Customer객체)
    @ManyToOne
    private Customer receiver;
    //메시지
    @Column(columnDefinition = "text")
    private String message;

    @Builder
    public Chat(Customer sender, Customer receiver, String message, ChatRoom chatRoom) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.chatRoom = chatRoom;
    }
}
