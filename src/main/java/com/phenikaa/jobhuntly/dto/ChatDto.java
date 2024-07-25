package com.phenikaa.jobhuntly.dto;

public class ChatDto {
    public record ChatRequest(
            Integer toUserId,
            Integer loggedInUserId,
            String message) {
    }

    public record ChatResponse() {

    }

    public record ChatRoomResponse(
            Integer id,
            UserDTO.UserResponse user1,
            UserDTO.UserResponse user2,
            boolean isUser1Seen,
            boolean isUser2Seen
    ) {}

    public record MessageResponse(
            Integer id,
            UserDTO.UserResponse user,
            String content
    ) {}
}
