package com.phenikaa.jobhuntly.repository;

import com.phenikaa.jobhuntly.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
    Optional<ChatRoom> findByUser1IdAndUser2Id(Integer user1Id, Integer user2Id);
}
