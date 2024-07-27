package com.phenikaa.jobhuntly.repository;

import com.phenikaa.jobhuntly.entity.ChatRoom;
import com.phenikaa.jobhuntly.entity.Company;
import com.phenikaa.jobhuntly.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
    Optional<ChatRoom> findByCompanyAndUser(Company company, User user);

}
