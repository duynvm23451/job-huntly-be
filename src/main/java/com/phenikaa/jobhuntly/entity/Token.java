package com.phenikaa.jobhuntly.entity;

import com.phenikaa.jobhuntly.enums.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String token;

    private Date expirationTime;

    private static final int EXPIRATION_DURATION  = 15;

    private TokenType tokenType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Token(User user, String token, TokenType tokenType ) {
        this.token = token;
        this.expirationTime = generateExpirationDate();
        this.tokenType = tokenType;
        this.user = user;
    }

    public static Date generateExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_DURATION);
        return new Date(calendar.getTimeInMillis());
    }
}
