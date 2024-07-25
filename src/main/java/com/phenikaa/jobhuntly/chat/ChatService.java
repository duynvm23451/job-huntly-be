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

import java.util.List;
import java.util.Set;

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
            chatRoom = chatRoomRepository.findByUser1AndUser2(toUser, loggedInUser).orElseGet(
                    () -> {
                        ChatRoom newChatRoom = new ChatRoom();
                        newChatRoom.setUser1(toUser);
                        newChatRoom.setUser2(loggedInUser);
                        return chatRoomRepository.save(newChatRoom);
                    }
            );
            chatRoom.setIsUser1Seen(false);
            chatRoom.setIsUser2Seen(true);
        }
        if (toUser.getRole() == Role.RECRUITER) {
            chatRoom = chatRoomRepository.findByUser1AndUser2(loggedInUser, toUser).orElseGet(
                    () -> {
                        ChatRoom newChatRoom = new ChatRoom();
                        newChatRoom.setUser1(loggedInUser);
                        newChatRoom.setUser2(toUser);
                        return chatRoomRepository.save(newChatRoom);
                    }
            );
            chatRoom.setIsUser1Seen(true);
            chatRoom.setIsUser2Seen(false);
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
            return user.getChatRoomsAsUser1();
        }
        if (user.getRole() == Role.RECRUITER) {
            return user.getChatRoomsAsUser2();
        }
        return null;
    }

    public List<Message> getMessagesList(Integer chatRoomId, User user) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
                () -> new ObjectNotFoundException("Phòng chat", chatRoomId)
        );
        if (user.getRole() == Role.EMPLOYEE) {
            chatRoom.setIsUser1Seen(true);
        }
        if (user.getRole() == Role.RECRUITER) {
            chatRoom.setIsUser2Seen(false);
        }
        chatRoomRepository.save(chatRoom);
        return messageRepository.findByChatRoomId(chatRoomId);
    }
}
