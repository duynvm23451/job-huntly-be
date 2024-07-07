package com.phenikaa.jobhuntly.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

public class ExchangeTokenDTO {
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record ExchangeTokenRequest(
            String code,
            String clientId,
            String clientSecret,
            String redirectUri,
            String grantType
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record ExchangeTokenResponse(
            String accessToken,
            String expiresIn,
            String refreshToken,
            String scope,
            String tokenType
    ) {}
}