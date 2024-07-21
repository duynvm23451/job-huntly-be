package com.phenikaa.jobhuntly.chat;

import com.phenikaa.jobhuntly.auth.AuthUser;
import com.phenikaa.jobhuntly.auth.JpaUserDetailsService;
import com.phenikaa.jobhuntly.auth.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketFilter implements ChannelInterceptor {

    private final JwtProvider jwtProvider;
    private final JpaUserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        assert accessor != null;
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String jwt = jwtProvider.parseToken(accessor);
            if (jwt != null && jwtProvider.validateToken(jwt)) {
                String email = jwtProvider.getUserEmail(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                accessor.setUser(authentication);
            }
        }
        return message;
    }
}
