package com.example.dm.chat.repository;

import com.example.dm.chat.entity.ChatRooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRooms, Long> {
}
