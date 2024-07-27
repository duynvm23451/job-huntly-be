package com.phenikaa.jobhuntly.dto;

public class ChatDto {
    public record ChatRequest(
            Integer destinationId,
            Integer loggedInUserId,
            String message) {
    }

    public record ChatResponse() {

    }

    public record ChatRoomResponse(
            Integer id,
            UserDTO.UserResponse user,
            CompanyDTO.ListCompanyResponse company,
            boolean isUserSeen,
            boolean isCompanySeen
    ) {}

    public record MessageResponse(
            Integer id,
            UserDTO.UserResponse user,
            String content
    ) {}
}
