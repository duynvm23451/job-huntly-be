package com.phenikaa.jobhuntly.service;

import com.phenikaa.jobhuntly.entity.Token;
import com.phenikaa.jobhuntly.entity.User;
import com.phenikaa.jobhuntly.enums.TokenType;
import com.phenikaa.jobhuntly.exception.ObjectNotFoundException;
import com.phenikaa.jobhuntly.repository.TokenRepository;
import com.phenikaa.jobhuntly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveToken(User user, String token, TokenType type) {
        Token existingToken = tokenRepository.findByTokenTypeAndUserId(type, user.getId()).orElseGet(
                () -> tokenRepository.save(new Token(user, token, type))
        );
        existingToken.setToken(token);
        existingToken.setExpirationTime(Token.generateExpirationDate());
        tokenRepository.save(existingToken);
    }

    public void checkToken(String token, TokenType tokenType) {
        User user = validateToken(token, tokenType);
        user.setEnable(true);
        userRepository.save(user);
    }

    public void resetUserPassword(String password, String token, TokenType tokenType) {
        User user = validateToken(token, tokenType);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    private User validateToken(String token, TokenType tokenType) {
        Token theToken = tokenRepository.findByTokenAndTokenType(token, tokenType)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy token"));

        if (theToken.getExpirationTime().before(new Date())) {
            tokenRepository.delete(theToken);
            throw new ObjectNotFoundException("token", "giá trị", token);
        }

        tokenRepository.delete(theToken);
        return theToken.getUser();
    }
}
