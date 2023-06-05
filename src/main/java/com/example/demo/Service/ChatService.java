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


    @Transactional
    public ChatReceiveDto save(long id, String nickname, ChatSendDto sendDto){
        ChatRoom chatRoom=chatRoomRepository.findById(id).orElseThrow(null);

        String receiveName= URLDecoder.decode(nickname, StandardCharsets.UTF_8);
        String sendName=URLDecoder.decode(sendDto.getSenderNickname(), StandardCharsets.UTF_8);
        Customer sender=customerRepository.findByUserId(sendName).get(0);
        Customer receiver=customerRepository.findByUserId(receiveName).get(0);

        Chat chat=Chat.builder()
                .chatRoom(chatRoom)
                .message(sendDto.getMessage())
                .sender(sender)
                .receiver(receiver).build();

        return new ChatReceiveDto(chatRepository.save(chat));
    }

    @Transactional
    public Long ChatRoomSave(String userId,String myUserId){
        ChatRoom chatRoom = new ChatRoom();
        long result=0;
        //기존 방이 있는지 확인
        int myId=customerRepository.findByUserId(myUserId).get(0).getId();
        int opponentId=customerRepository.findByUserId(userId).get(0).getId();

        //있으면 기존 방 번호를 전달
        Optional<Chat> chat=chatRepository.findByIdsFromRoomId(myId, opponentId);
        if(!chat.isPresent()){
            log.info(userId+" 새로운 방");
            result = chatRoomRepository.save(chatRoom).getRoomId();
        } else {
            log.info(userId+" 이미 존재하는 방이 있음");
            result = chat.get().getChatRoom().getRoomId();
        }
        return result;
    }
    @Transactional(readOnly = true)
    public List<ChatUserDto> findByLastChat(int myId,String myUserId){
        return chatRepository.getLastMessage(myId).stream().map(chat->new ChatUserDto(chat,myUserId)).collect(Collectors.toList());
    }
    @Transactional
    public void deleteEmptyChatRoom(){
        chatRoomRepository.findChatroomsWithNoMatchingChats().forEach(chatRoomRepository::delete);
    }
    @Transactional
    public List<ChatChattedDto> getChatList(long id){
        List<Chat> chatList=chatRoomRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id)).getChatList();
        return chatList.stream().map(ChatChattedDto::new).collect(Collectors.toList());
    }
}
