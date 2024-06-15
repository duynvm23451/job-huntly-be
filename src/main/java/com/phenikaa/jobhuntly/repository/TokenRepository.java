package com.phenikaa.jobhuntly.repository;

import com.phenikaa.jobhuntly.entity.Token;
import com.phenikaa.jobhuntly.enums.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByTokenAndTokenType(String token, TokenType tokenType);
}
