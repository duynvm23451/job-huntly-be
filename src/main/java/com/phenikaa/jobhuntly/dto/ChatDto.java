package com.phenikaa.jobhuntly.dto;

public class ChatDto {
    public record ChatRequest(
            Integer toUserId,
            Integer loggedInUserId,
            String message) {
    }

    public record ChatResponse() {

    }
}
