package com.phenikaa.jobhuntly.chat;

import com.phenikaa.jobhuntly.dto.ChatDto;
import com.phenikaa.jobhuntly.dto.ResponseDTO;
import com.phenikaa.jobhuntly.exception.ObjectNotFoundException;
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

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat")
    public void sendMessage(@Payload ChatDto.ChatRequest chatRequest) {
        try {;
            chatService.sendMessage(chatRequest.toUserId(), chatRequest.message(), chatRequest.loggedInUserId());
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
}
