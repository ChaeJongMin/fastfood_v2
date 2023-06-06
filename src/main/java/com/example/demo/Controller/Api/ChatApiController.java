package com.example.demo.Controller.Api;

import com.example.demo.Service.ChatService;
import com.example.demo.Service.CustomerService;
import com.example.demo.dto.chat.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
public class ChatApiController {
    private final CustomerService customerService;
    private final ChatService chatService;
    // @EnableWebSocketMessageBroker를 통해서 등록되는 bean이며 특정 Broker로(chatConfig에 미리 설정) 메시지를 전달
    private final SimpMessagingTemplate template;


    /*
     Client 가 SEND 할 시 작동하는 메소드이며 이 떄 chatConfig에 선언한 prefix로 주소로 오면 작동
     @MessageMapping은 WebSocket 으로 들어오는 메세지 발행을 처리
     기존의 핸들러 역할을 대신해주고 있도록 만들었다.
     실질적으로 send/room/{roomId}/{receiver} 주소로 메시지를 전달
     */
    @MessageMapping("/room/{roomId}/{receiver}")
    public void message(@RequestBody ChatSendDto sendDto, @DestinationVariable Long roomId, @DestinationVariable String receiver) throws Exception{
        //받아온 메시지를 구독자에게 전달한 dto로 만든다.
        ChatReceiveDto receiveDto = chatService.save(roomId, receiver, sendDto);
        //구독된 주소 (/receive/room/roomid/opponentName)으로 dto를 전달
        template.convertAndSend("/receive/room/"+roomId+"/"+receiveDto.getReceiverUser(), receiveDto);
        log.info("전달한 구독 url: "+"/receive/room/"+roomId+"/"+receiveDto.getReceiverUser());
    }

    //채팅방을 생성하는 메소드
    @PostMapping("/api/chat/{nickname}/{myUserId}")
    public long saveChatRoom(@PathVariable String nickname,@PathVariable String myUserId){
        log.info("새로운 방 생성!!");
        return chatService.ChatRoomSave(nickname,myUserId);
    }

    //검색 필드에 유저를 검색할 시 유저들을 찾는 메소드
    @GetMapping("/api/chat/find/{userId}/{myId}")
    public ResponseEntity<?> findUser(@PathVariable String userId,@PathVariable int myId){
        log.info("findUser: "+userId);
        //키워드인 userId를 통해 키워드가 포함된 유저들의 리스트를 얻어온다.
        List<ChatFindUserResponseDto> findList=customerService.findUserName(userId,myId);
        log.info("findUser: "+findList.get(0).getUserName());
        //reponse에 담아서 전달
        return ResponseEntity.status(HttpStatus.OK).body(findList);
    }

    //이미 대화한 유저들의 정보를 가져오는 메소드
    @GetMapping("/api/chat/chatted/{id}/{myUserId}")
    public ResponseEntity<?> findOpponentUser(@PathVariable int id, @PathVariable String myUserId){
        log.info("findOpponentUser 실행");
        //상대방 정보를 얻어옵니다.
        List<ChatUserDto> opponentList=chatService.findByLastChat(id,myUserId);
        //reponse에 담아서 전달
        return ResponseEntity.status(HttpStatus.OK).body(opponentList);
    }

    // 해당 유저와의 채팅 메시지를 얻어오는 메소드
    @GetMapping("/api/chat/load/{roomId}")
    public ResponseEntity<?> loadMeAndYouMessage(@PathVariable long roomId){
        //roomId를 통해 지금까지 채팅한 메시지를 얻어온다.
        List<ChatChattedDto> chattedDtos=chatService.getChatList(roomId);
        //reponse에 담아서 전달
        return ResponseEntity.status(HttpStatus.OK).body(chattedDtos);
    }

}
