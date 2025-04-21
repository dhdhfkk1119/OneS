package com.example.ones.CustomHander;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //연결할 endpoint ("/ws")
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") //CORS 허용
                .withSockJS(); // SockJs 지원
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트에서 구독할 경로
        registry.enableSimpleBroker("/topic");
        
        // 클라이언트에서 메시지 보낼 때 사용하는 prefix
        registry.setApplicationDestinationPrefixes("/app");
    }
}
