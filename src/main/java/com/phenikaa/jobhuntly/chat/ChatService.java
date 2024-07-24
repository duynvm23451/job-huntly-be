package com.phenikaa.jobhuntly.chat;

import com.phenikaa.jobhuntly.entity.ChatRoom;
import com.phenikaa.jobhuntly.entity.Message;
import com.phenikaa.jobhuntly.entity.User;
import com.phenikaa.jobhuntly.enums.Role;
import com.phenikaa.jobhuntly.exception.ObjectNotFoundException;
import com.phenikaa.jobhuntly.repository.ChatRoomRepository;
import com.phenikaa.jobhuntly.repository.MessageRepository;
import com.phenikaa.jobhuntly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    public void sendMessage(Integer toUserId, String message, Integer loggedInUserId) {
        User toUser = userRepository.findById(toUserId).orElseThrow(
                () -> new ObjectNotFoundException("Người dùng", toUserId)
        );
        User loggedInUser = userRepository.findById(loggedInUserId).orElseThrow(
                () -> new ObjectNotFoundException("Người dùng", loggedInUserId)
        );
        ChatRoom chatRoom = null;
        if (toUser.getRole() == Role.EMPLOYEE) {
            chatRoom = chatRoomRepository.findByUser1IdAndUser2Id(toUserId, loggedInUserId).orElseGet(
                    () -> {
                        ChatRoom newChatRoom = new ChatRoom();
                        newChatRoom.setUser1(toUser);
                        newChatRoom.setUser2(loggedInUser);
                        return chatRoomRepository.save(newChatRoom);
                    }
            );
        }
        if (toUser.getRole() == Role.RECRUITER) {
            chatRoom = chatRoomRepository.findByUser1IdAndUser2Id(toUserId, loggedInUserId).orElseGet(
                    () -> {
                        ChatRoom newChatRoom = new ChatRoom();
                        newChatRoom.setUser1(loggedInUser);
                        newChatRoom.setUser2(toUser);
                        return chatRoomRepository.save(newChatRoom);
                    }
            );
        }
        Message newMessage = new Message();
        newMessage.setChatRoom(chatRoom);
        newMessage.setUser(loggedInUser);
        newMessage.setContent(message);
        messageRepository.save(newMessage);

    }
}
