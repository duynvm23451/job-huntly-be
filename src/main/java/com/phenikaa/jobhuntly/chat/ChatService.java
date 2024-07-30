package com.phenikaa.jobhuntly.chat;

import com.phenikaa.jobhuntly.entity.*;
import com.phenikaa.jobhuntly.enums.Role;
import com.phenikaa.jobhuntly.exception.ObjectNotFoundException;
import com.phenikaa.jobhuntly.exception.SharedException;
import com.phenikaa.jobhuntly.repository.ChatRoomRepository;
import com.phenikaa.jobhuntly.repository.CompanyRepository;
import com.phenikaa.jobhuntly.repository.MessageRepository;
import com.phenikaa.jobhuntly.repository.UserRepository;
import com.phenikaa.jobhuntly.specification.ChatRoomSpecification;
import com.phenikaa.jobhuntly.specification.filter.ChatRoomFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public void sendMessage(Integer chatRoomId, String message, Integer loggedInUserId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
                () -> new ObjectNotFoundException("Phòng chat", chatRoomId)
        );
        User user = userRepository.findById(loggedInUserId).orElseThrow(
                () -> new ObjectNotFoundException("Người dùng", loggedInUserId)
        );
        Message newMessage = new Message();
        newMessage.setChatRoom(chatRoom);
        newMessage.setUser(user);
        newMessage.setContent(message);
        messageRepository.save(newMessage);

    }

    public Page<ChatRoom> getChatRoom(User user, ChatRoomFilter filter, Pageable pageable) {
        Specification<ChatRoom> specification = Specification.where(null);
        if (user.getRole() == Role.EMPLOYEE) {
            specification = specification.and(ChatRoomSpecification.byUserId(user.getId()));
            if (filter.getCompanyName() != null) {
                specification = specification.and(ChatRoomSpecification.containsCompanyName(filter.getCompanyName()));
            }
        }
        if (user.getRole() == Role.RECRUITER) {
            specification = specification.and(ChatRoomSpecification.byCompanyId(user.getCompany().getId()));
            if (filter.getUserFullName() != null) {
                specification = specification.and(ChatRoomSpecification.containsUserFullName(filter.getUserFullName()));
            }
        }
        return chatRoomRepository.findAll(specification, pageable);
    }

    public Page<Message> getMessagesList(Integer chatRoomId, User user, Pageable pageable) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
                () -> new ObjectNotFoundException("Phòng chat", chatRoomId)
        );
        if (user.getRole() == Role.EMPLOYEE) {
            chatRoom.setIsUserSeen(true);
        }
        if (user.getRole() == Role.RECRUITER) {
            chatRoom.setIsCompanySeen(true);
        }
        chatRoomRepository.save(chatRoom);
        Specification<Message> specification = Specification.where((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("chatRoom").get("id"), chatRoomId));

        return messageRepository.findAll(specification, pageable);
    }

    public ChatRoom createChatRoom(User user, Integer destinationId) {
        ChatRoom chatRoom = null;
        AtomicBoolean isExisting = new AtomicBoolean(true);
        if (user.getRole() == Role.EMPLOYEE) {
            Company company = companyRepository.findById(destinationId).orElseThrow(
                    () -> new ObjectNotFoundException("Công ty", destinationId)
            );
            chatRoom = chatRoomRepository.findByCompanyAndUser(company, user).orElseGet(
                    () -> {
                        isExisting.set(false);
                        ChatRoom newChatRoom = new ChatRoom();
                        newChatRoom.setCompany(company);
                        newChatRoom.setUser(user);
                        return chatRoomRepository.save(newChatRoom);
                    }
            );
            if (!isExisting.get()) {
                chatRoom.setIsCompanySeen(false);
                chatRoom.setIsUserSeen(true);
            }

        }
        if (user.getRole() == Role.RECRUITER) {
            User recevier = userRepository.findById(destinationId).orElseThrow(
                    () -> new ObjectNotFoundException("Người dùng", destinationId)
            );
            chatRoom = chatRoomRepository.findByCompanyAndUser(user.getCompany(), recevier).orElseGet(
                    () -> {
                        isExisting.set(false);
                        ChatRoom newChatRoom = new ChatRoom();
                        newChatRoom.setCompany(user.getCompany());
                        newChatRoom.setUser(recevier);
                        return chatRoomRepository.save(newChatRoom);
                    }
            );
            if (!isExisting.get()) {
                chatRoom.setIsCompanySeen(true);
                chatRoom.setIsUserSeen(false);
            }
        }
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom getChatRoomById(User user, Integer chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
                () -> new ObjectNotFoundException("Phòng chat", chatRoomId)
        );
        if (user.getRole() == Role.EMPLOYEE) {
            if (!chatRoom.getUser().equals(user)) {
                throw new SharedException("Tài khoản không có quyền truy cập vào phòng chat này");
            }
        }
        if (user.getRole() == Role.RECRUITER) {
            if (!chatRoom.getCompany().equals(user.getCompany())) {
                throw new SharedException("Tài khoản không có quyền truy cập vào phòng chat này");
            }
        }
        return chatRoom;
    }
}
