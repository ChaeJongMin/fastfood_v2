package com.example.demo.Service;

import com.example.demo.domain.Chat;
import com.example.demo.domain.ChatRoom;
import com.example.demo.domain.Customer;
import com.example.demo.dto.chat.ChatChattedDto;
import com.example.demo.dto.chat.ChatReceiveDto;
import com.example.demo.dto.chat.ChatSendDto;
import com.example.demo.dto.chat.ChatUserDto;
import com.example.demo.persistence.ChatRepository;
import com.example.demo.persistence.ChatRoomRepository;
import com.example.demo.persistence.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final CustomerRepository customerRepository;
    private final ChatRepository chatRepository;

    //채팅한 내용(chat)을 저장하는 메소드
    @Transactional
    public ChatReceiveDto save(long id, String nickname, ChatSendDto sendDto){
        //id(채팅방)으로 대화한 채팅방을 찾습니다.
        ChatRoom chatRoom=chatRoomRepository.findById(id).orElseThrow(null);
        //sender, receiver (customer 객체)를 찾습니다.
        String receiveName= URLDecoder.decode(nickname, StandardCharsets.UTF_8);
        String sendName=URLDecoder.decode(sendDto.getSenderNickname(), StandardCharsets.UTF_8);
        Customer sender=customerRepository.findByUserId(sendName).get(0);
        Customer receiver=customerRepository.findByUserId(receiveName).get(0);

        //chat 객체를 만들고 chat 테이블에 저장
        return new ChatReceiveDto(chatRepository.save(
                Chat.builder()
                        .chatRoom(chatRoom)
                        .message(sendDto.getMessage())
                        .sender(sender)
                        .receiver(receiver)
                        .build()
        ));
    }

    //채팅방을 저장하는 메소드
    //채팅을 시작할 시 해당 채팅방이 이미 존재하는지 없는지를 구별해야한다.
    @Transactional
    public Long ChatRoomSave(String userId,String myUserId){
        ChatRoom chatRoom = new ChatRoom();
        long result=0;
        //기존 방이 있는지 확인
        int myId=customerRepository.findByUserId(myUserId).get(0).getId();
        int opponentId=customerRepository.findByUserId(userId).get(0).getId();
        //상대방, 자신의 아이디를 전달하여 이미 채팅방이 있는지 조사 (1대1이어서 2개의 아이디만 있어도된다.)
        Optional<Chat> chat=chatRepository.findByIdsFromRoomId(myId, opponentId);
        //만약 없을 경우 새로 만든다.
        if(!chat.isPresent()){
            log.info(userId+" 새로운 방");
            result = chatRoomRepository.save(chatRoom).getRoomId();
        //이미 존재 시 존재한 채팅방 아이디를 넘긴다.
        } else {
            log.info(userId+" 이미 존재하는 방이 있음");
            result = chat.get().getChatRoom().getRoomId();
        }
        //채팅방 id를 반환
        return result;
    }

    //대화했던 상대방 정보를 얻는 메소드
    @Transactional(readOnly = true)
    public List<ChatUserDto> findByLastChat(int myId,String myUserId){
        //myId(자신의 기본키)로 chat list를 얻어와 ChatUserDto 리스트로 변환해서 반환
        return chatRepository.getLastMessage(myId).stream().map(chat->new ChatUserDto(chat,myUserId)).collect(Collectors.toList());
    }
    //채팅방을 삭제하는 메소드
    //제가 채팅방을 생성하는 로직이 페이지에서 채팅하기 버튼으로 채팅방이 생성
    //그러나 대화를 시도한 유저가 아무 메시지를 전송하지 않으면 쓸모없는 빈 방이 되므로 주기적으로 체크하여 삭제
    @Transactional
    public void deleteEmptyChatRoom(){
        //chat 테이블에 존재하는 chatroom id를 찾아 제거
        chatRoomRepository.findChatroomsWithNoMatchingChats().forEach(chatRoomRepository::delete);
    }

    //전에 대화한 대화를 가져오는 메소드
    @Transactional
    public List<ChatChattedDto> getChatList(long id){
        // ChatRoom(SLAVE)의 getChatList()는 기본적으로 FetchType이 LAZY입니다.
        // 그냥 가져올 시 LazyInitializationException 에러가 발생하기에 EAGER로 변경하거나 Transactional 내부에 미리 조회하는 방법이 존재
        // 후자 방법을 추천
        List<Chat> chatList=chatRoomRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id)).getChatList();
        return chatList.stream().map(ChatChattedDto::new).collect(Collectors.toList());
    }
}
