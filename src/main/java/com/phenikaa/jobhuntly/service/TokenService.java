package com.phenikaa.jobhuntly.service;

import com.phenikaa.jobhuntly.entity.Token;
import com.phenikaa.jobhuntly.entity.User;
import com.phenikaa.jobhuntly.enums.TokenType;
import com.phenikaa.jobhuntly.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public void saveToken(User user, String token, TokenType type) {
        Token tokenObj = new Token(user, token, type);
        tokenRepository.save(tokenObj);
    }
}
