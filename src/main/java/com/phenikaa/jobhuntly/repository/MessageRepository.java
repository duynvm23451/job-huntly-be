package com.phenikaa.jobhuntly.repository;

import com.phenikaa.jobhuntly.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByChatRoomId(Integer chatRoomId);
}
