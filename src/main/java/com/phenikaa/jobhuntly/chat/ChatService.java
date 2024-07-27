package com.phenikaa.jobhuntly.chat;

import com.phenikaa.jobhuntly.entity.ChatRoom;
import com.phenikaa.jobhuntly.entity.Company;
import com.phenikaa.jobhuntly.entity.Message;
import com.phenikaa.jobhuntly.entity.User;
import com.phenikaa.jobhuntly.enums.Role;
import com.phenikaa.jobhuntly.exception.ObjectNotFoundException;
import com.phenikaa.jobhuntly.repository.ChatRoomRepository;
import com.phenikaa.jobhuntly.repository.CompanyRepository;
import com.phenikaa.jobhuntly.repository.MessageRepository;
import com.phenikaa.jobhuntly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public void sendMessage(Integer destinationId, String message, Integer loggedInUserId) {
//        User toUser = userRepository.findById(toUserId).orElseThrow(
//                () -> new ObjectNotFoundException("Người dùng", toUserId)
//        );
        User loggedInUser = userRepository.findById(loggedInUserId).orElseThrow(
                () -> new ObjectNotFoundException("Người dùng", loggedInUserId)
        );
        ChatRoom chatRoom = null;
        if (loggedInUser.getRole() == Role.EMPLOYEE) {
            Company company = companyRepository.findById(destinationId).orElseThrow(
                    () -> new ObjectNotFoundException("Công ty", destinationId)
            );
            chatRoom = chatRoomRepository.findByCompanyAndUser(company, loggedInUser).orElseGet(
                    () -> {
                        ChatRoom newChatRoom = new ChatRoom();
                        newChatRoom.setCompany(company);
                        newChatRoom.setUser(loggedInUser);
                        return chatRoomRepository.save(newChatRoom);
                    }
            );
            chatRoom.setIsCompanySeen(false);
            chatRoom.setIsUserSeen(true);
        }
        if (loggedInUser.getRole() == Role.RECRUITER) {
            User recevier = userRepository.findById(destinationId).orElseThrow(
                    () -> new ObjectNotFoundException("Người dùng", destinationId)
            );
            chatRoom = chatRoomRepository.findByCompanyAndUser(loggedInUser.getCompany(), recevier).orElseGet(
                    () -> {
                        ChatRoom newChatRoom = new ChatRoom();
                        newChatRoom.setCompany(loggedInUser.getCompany());
                        newChatRoom.setUser(recevier);
                        return chatRoomRepository.save(newChatRoom);
                    }
            );
            chatRoom.setIsCompanySeen(true);
            chatRoom.setIsUserSeen(false);
        }
        chatRoomRepository.save(chatRoom);
        Message newMessage = new Message();
        newMessage.setChatRoom(chatRoom);
        newMessage.setUser(loggedInUser);
        newMessage.setContent(message);
        messageRepository.save(newMessage);

    }

    public Set<ChatRoom> getChatRoom(User user) {
        if (user.getRole() == Role.EMPLOYEE) {
            return user.getChatRoom();
        }
        if (user.getRole() == Role.RECRUITER) {

            return user.getCompany().getChatRoom();
        }
        return null;
    }

    public List<Message> getMessagesList(Integer chatRoomId, User user) {
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
        return messageRepository.findByChatRoomId(chatRoomId);
    }
}
