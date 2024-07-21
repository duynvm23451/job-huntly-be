package com.phenikaa.jobhuntly.chat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.RequestUpgradeStrategy;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${frontend.caller.host:http://localhost.5173}")
    private String frontendCallerHost;

    private final WebSocketFilter webSocketFilter;

    public WebSocketConfig(WebSocketFilter webSocketConfig) {
        this.webSocketFilter = webSocketConfig;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketFilter);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        RequestUpgradeStrategy upgradeStrategy = new TomcatRequestUpgradeStrategy();
        registry.addEndpoint("/ws")
                .setHandshakeHandler(new DefaultHandshakeHandler(upgradeStrategy))
                .setAllowedOrigins(this.frontendCallerHost);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app")
                .enableSimpleBroker("/topic")
                .setTaskScheduler(heartbeatTaskScheduler())
                .setHeartbeatValue(new long[] {10000L, 10000L})
        ;
    }

    @Bean
    public TaskScheduler heartbeatTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }
}
