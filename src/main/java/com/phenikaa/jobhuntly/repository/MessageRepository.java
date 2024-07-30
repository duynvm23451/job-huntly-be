package com.phenikaa.jobhuntly.repository;

import com.phenikaa.jobhuntly.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Integer>, JpaSpecificationExecutor<Message> {
    List<Message> findByChatRoomId(Integer chatRoomId);
}
