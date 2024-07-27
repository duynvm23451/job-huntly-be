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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        try {;
            chatService.sendMessage(chatRequest.destinationId(), chatRequest.message(), chatRequest.loggedInUserId());
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .success(true)
                    .code(HttpStatus.OK.value())
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

    @GetMapping("/chatRooms")
    public ResponseDTO getChatRooms(@AuthenticationPrincipal Jwt jwt) {
        User user = userService.getUser(jwt.getClaim("email"));
        Set<ChatRoom> chatRooms = chatService.getChatRoom(user);
        List<ChatDto.ChatRoomResponse> response = chatRooms.stream()
                .map(chatRoomMapper::toChatRoomResponse)
                .toList();
        return ResponseDTO.builder()
                .success(true)
                .message("Lấy thông tin phòng chat thành công")
                .code(HttpStatus.OK.value())
                .data(response)
                .build();

    }

    @GetMapping("messages/{chatRoomId}")
    public ResponseDTO getMessages(@PathVariable Integer chatRoomId, @AuthenticationPrincipal Jwt jwt) {
        User user = userService.getUser(jwt.getClaim("email"));
        List<Message> messages = chatService.getMessagesList(chatRoomId, user);
        List<ChatDto.MessageResponse> response = messages.stream()
                .map(messageMapper::toMessageResponse)
                .toList();
        return ResponseDTO.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Lấy danh sách tin nhắn")
                .data(response)
                .build();
    }
}
