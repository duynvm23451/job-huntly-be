package com.phenikaa.jobhuntly.service;

import com.phenikaa.jobhuntly.entity.Token;
import com.phenikaa.jobhuntly.entity.User;
import com.phenikaa.jobhuntly.enums.TokenType;
import com.phenikaa.jobhuntly.repository.TokenRepository;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private TokenRepository tokenRepository;

    public void saveToken(User user, String token, TokenType type) {
        Token tokenObj = new Token(user, token, type);
        tokenRepository.save(tokenObj);
    }
}
