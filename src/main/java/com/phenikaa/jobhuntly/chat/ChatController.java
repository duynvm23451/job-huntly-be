package com.phenikaa.jobhuntly.chat;

import com.phenikaa.jobhuntly.dto.ChatDto;
import com.phenikaa.jobhuntly.dto.ResponseDTO;
import com.phenikaa.jobhuntly.entity.ChatRoom;
import com.phenikaa.jobhuntly.entity.Message;
import com.phenikaa.jobhuntly.entity.User;
import com.phenikaa.jobhuntly.exception.ObjectNotFoundException;
import com.phenikaa.jobhuntly.mapper.ChatRoomMapper;
import com.phenikaa.jobhuntly.mapper.MessageMapper;
import com.phenikaa.jobhuntly.service.UserService;
import com.phenikaa.jobhuntly.specification.filter.ChatRoomFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final UserService userService;
    private final ChatRoomMapper chatRoomMapper;
    private final MessageMapper messageMapper;

    @MessageMapping("/chat")
    public void sendMessage(@Payload ChatDto.ChatRequest chatRequest) {
        try {
            System.out.println(chatRequest.chatRoomId());
            System.out.println(chatRequest.message());
            System.out.println(chatRequest.loggedInUserId());
            Message message = chatService.sendMessage(chatRequest.chatRoomId() ,chatRequest.message(), chatRequest.loggedInUserId());
            ChatDto.MessageResponse response = messageMapper.toMessageResponse(message);
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .success(true)
                    .code(HttpStatus.OK.value())
                    .data(response)
                    .message("Gửi tin nhắn thành công")
                    .build();
            messagingTemplate.convertAndSend("/topic/messages", responseDTO);
        } catch (ObjectNotFoundException e) {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .success(false)
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Gửi tin nhắn thất bại")
                    .data(e)
                    .build();
            messagingTemplate.convertAndSend("/topic/messages", responseDTO);
        }
    }

    @PostMapping("/chatRooms")
    public ResponseDTO createChatRoom(@AuthenticationPrincipal Jwt jwt, Integer destinationId) {
        User user = userService.getUser(jwt.getClaim("email"));
        Integer chatRoomId = chatService.createChatRoom(user, destinationId).getId();
        return ResponseDTO.builder()
                .success(true)
                .message("Tạo phòng chat thành công")
                .code(HttpStatus.OK.value())
                .data(chatRoomId)
                .build();
    }

    @GetMapping("/chatRooms")
    public ResponseDTO getChatRooms(@AuthenticationPrincipal Jwt jwt, ChatRoomFilter filter, Pageable pageable) {
        User user = userService.getUser(jwt.getClaim("email"));
        Page<ChatRoom> chatRooms = chatService.getChatRoom(user, filter, pageable);
        List<ChatDto.ChatRoomResponse> response = chatRooms
                .map(chatRoomMapper::toChatRoomResponse)
                .toList();
        return ResponseDTO.builder()
                .success(true)
                .message("Lấy thông tin phòng chat thành công")
                .code(HttpStatus.OK.value())
                .data(response)
                .build();

    }

    @GetMapping("chatRooms/{chatRoomId}")
    public ResponseDTO getChatRoom(@AuthenticationPrincipal Jwt jwt, @PathVariable Integer chatRoomId) {
        User user = userService.getUser(jwt.getClaim("email"));
        ChatRoom chatRoom = chatService.getChatRoomById(user, chatRoomId);
        ChatDto.ChatRoomResponse chatRoomResponse = chatRoomMapper.toChatRoomResponse(chatRoom);
        return ResponseDTO.builder()
                .success(true)
                .message("Lấy thông tin phòng chat theo Id thành công")
                .code(HttpStatus.OK.value())
                .data(chatRoomResponse)
                .build();
    }

    @GetMapping("messages/{chatRoomId}")
    public ResponseDTO getMessages(@PathVariable Integer chatRoomId, @AuthenticationPrincipal Jwt jwt, Pageable pageable) {
        User user = userService.getUser(jwt.getClaim("email"));
        Page<Message> messages = chatService.getMessagesList(chatRoomId, user, pageable);
        Page<ChatDto.MessageResponse> response = messages.map(messageMapper::toMessageResponse);
        return ResponseDTO.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Lấy danh sách tin nhắn")
                .data(response)
                .build();
    }
}
