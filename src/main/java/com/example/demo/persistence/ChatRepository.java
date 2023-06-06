package com.example.demo.persistence;


import com.example.demo.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat,Long> {
    // 가장 최근 메시지를 보낸 Chat 객체를 얻는 쿼리메소드
    // "chat" 테이블에서 최신 대화를 검색하고, 해당 대화가 특정 "roomId"와 연결되어 있으며, "sender" 또는 "receiver" 중 하나의 ID가 ":myId"와 일치하는 chat를 반환
    @Query("SELECT chat FROM Chat chat WHERE chat.createDates = (SELECT MAX(c.createDates) FROM Chat c WHERE  (c.chatRoom.roomId = chat.chatRoom.roomId) and (chat.sender.id = :myId OR chat.receiver.id = :myId))")
    List<Chat> getLastMessage(@Param("myId") int myId);

    // 해당 유저 아이디, 상대방 아이디가 존재하는 채팅방이 있는지 찾는 쿼리메소드
    // "myId"와 "opponentId"에 해당하는 대화를 검색하고, 해당 대화를 chatRoom으로 그룹화한 후 중복을 제거하여 반환
    @Query("select DISTINCT ch from Chat ch where (ch.sender.id = :myId or ch.sender.id =:opponentId) and (ch.receiver.id =:myId or ch.receiver.id =:opponentId) group by ch.chatRoom")
    Optional<Chat> findByIdsFromRoomId(@Param("myId") int myId, @Param("opponentId") int opponentId);

}
