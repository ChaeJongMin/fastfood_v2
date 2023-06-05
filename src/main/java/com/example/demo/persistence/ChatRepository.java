package com.example.demo.persistence;


import com.example.demo.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat,Long> {

    @Query("SELECT chat FROM Chat chat WHERE chat.createDates = (SELECT MAX(c.createDates) FROM Chat c WHERE  (c.chatRoom.roomId = chat.chatRoom.roomId) and (chat.sender.id = :myId OR chat.receiver.id = :myId))")
    List<Chat> getLastMessage(@Param("myId") int myId);

    @Query("select chat from Chat chat where chat.sender.id = :myId or chat.receiver.id = :myId")
    List<Chat> findBySenderOrReceiver(@Param("myId") int myId);

    @Query("select DISTINCT ch from Chat ch where (ch.sender.id = :myId or ch.sender.id =:opponentId) and (ch.receiver.id =:myId or ch.receiver.id =:opponentId) group by ch.chatRoom")
    Optional<Chat> findByIdsFromRoomId(@Param("myId") int myId, @Param("opponentId") int opponentId);


}
