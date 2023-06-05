package com.example.demo.Controller.Api;

import com.example.demo.Service.ChatService;
import com.example.demo.Service.CustomerService;
import com.example.demo.dto.chat.*;
import com.example.demo.exception.UserAuthException;
import com.example.demo.exception.message.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
public class ChatApiController {
    private final CustomerService customerService;
    private final ChatService chatService;
    private final SimpMessagingTemplate template;
//
//    @GetMapping("/api/chat/recent/{id}")
//    public ResponseEntity<?> recentUser(@PathVariable Long id){
//
//    }
    @MessageMapping("/room/{roomId}/{receiver}")
    public void message(@RequestBody ChatSendDto sendDto, @DestinationVariable Long roomId, @DestinationVariable String receiver) throws Exception{
        log.info("메시지를 받아왔어욨!!!!!!!");
        ChatReceiveDto receiveDto = chatService.save(roomId, receiver, sendDto);

//        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
//        headerAccessor.setSessionId(receiver);
//        headerAccessor.setLeaveMutable(true);
//        template.convertAndSend("/api/chat/receive/"+receiveDto.getReceiverUser(), receiveDto);
        template.convertAndSend("/receive/room/"+roomId+"/"+receiveDto.getReceiverUser(), receiveDto);
//        template.convertAndSend("/receive/room/set", "안녕");
        log.info("전달한 구독 url: "+"/receive/room/"+roomId+"/"+receiveDto.getReceiverUser());
    }

    @PostMapping("/api/chat/{nickname}/{myUserId}")
    public long saveChatRoom(@PathVariable String nickname,@PathVariable String myUserId){
        log.info("새로운 방 생성!!");
        return chatService.ChatRoomSave(nickname,myUserId);
    }


    @GetMapping("/api/chat/find/{userId}/{myId}")
    public ResponseEntity<?> findUser(@PathVariable String userId,@PathVariable int myId){
        log.info("findUser: "+userId);
        //customer 서비스에 유저 찾기 로직

        List<ChatFindUserResponseDto> findList=customerService.findUserName(userId,myId);
        log.info("findUser: "+findList.get(0).getUserName());
        //reponse에 담아서 출력
        return ResponseEntity.status(HttpStatus.OK).body(findList);
    }

    @GetMapping("/api/chat/chatted/{id}/{myUserId}")
    public ResponseEntity<?> findOpponentUser(@PathVariable int id, @PathVariable String myUserId){
        log.info("findOpponentUser 실행");
        List<ChatUserDto> opponentList=chatService.findByLastChat(id,myUserId);
        return ResponseEntity.status(HttpStatus.OK).body(opponentList);
    }
    //

    @GetMapping("/api/chat/load/{roomId}")
    public ResponseEntity<?> loadMeAndYouMessage(@PathVariable long roomId){
        List<ChatChattedDto> chattedDtos=chatService.getChatList(roomId);
        return ResponseEntity.status(HttpStatus.OK).body(chattedDtos);
    }

}
