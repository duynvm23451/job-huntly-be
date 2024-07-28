package com.phenikaa.jobhuntly.repository;

import com.phenikaa.jobhuntly.entity.ChatRoom;
import com.phenikaa.jobhuntly.entity.Company;
import com.phenikaa.jobhuntly.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer>, JpaSpecificationExecutor<ChatRoom> {
    Optional<ChatRoom> findByCompanyAndUser(Company company, User user);

}
