package com.example.demo.persistence;


import com.example.demo.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long>  {
    //방은 존재하나 채팅이 없는 방(쓸모없는 방이므로 삭제)
    //주기적으로 검사하여 삭제 필요!
    @Query("SELECT cr FROM ChatRoom cr WHERE cr.roomId NOT IN (SELECT DISTINCT ch.chatRoom.roomId FROM Chat ch)")
    List<ChatRoom> findChatroomsWithNoMatchingChats();
}
