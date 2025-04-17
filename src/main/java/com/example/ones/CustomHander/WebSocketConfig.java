package com.example.ones.CustomHander;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration // 이 클래스는 Spring 설정 파일이라는 뜻이다
@EnableWebSocketMessageBroker // WebSocket 메세지 기능(STOMP 포함)을 활성화 한다는 뜻
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 연결할 엔드포인트 등록
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메세지를 받을 경로
        registry.enableSimpleBroker("/topic", "/queue"); // 구독용
        registry.setApplicationDestinationPrefixes("/app"); // 발신용
        registry.setUserDestinationPrefix("/user"); // 쪽지용
    }
}
