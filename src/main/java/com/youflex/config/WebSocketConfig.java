package com.youflex.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration // 스프링에게 이 클래스가 '설정 파일'임을 알려줍니다.
@EnableWebSocketMessageBroker // 웹소켓을 기반으로 한 실시간 메시지 브로커(STOMP) 기능을 활성화합니다.
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // [클라이언트 연결 설정]
        // 프론트엔드(브라우저)에서 처음 웹소켓 무전기를 켜고 서버에 연결을 요청할 주소(Endpoint)를 지정합니다.
        registry.addEndpoint("/ws-connect")
                
                // [CORS 허용]
                // 원래 웹은 주소가 다르면 통신을 막지만, 외부(다른 포트나 도메인)에서도 연결할 수 있도록 허용합니다.
                .setAllowedOriginPatterns("*")
                
                // [호환성 지원]
                // 웹소켓을 지원하지 않는 구형 브라우저(Internet Explorer 등)에서도 
                // 일반 HTTP 통신을 소켓처럼 흉내 내어 연결이 유지되도록 도와주는 라이브러리(SockJS)를 적용합니다.
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // [메시지 구독(수신) 경로 설정 - Incoming/Outgoing]
        // 서버가 클라이언트에게 실시간으로 메시지를 밀어줄(Push) 때 사용할 주소 접두사입니다.
        // 프론트엔드에서는 주로 "/sub/chatroom/방번호" 형태로 이 주소를 '구독'하고 대기합니다.
        registry.enableSimpleBroker("/sub");
        
        // [메시지 발행(발신) 경로 설정 - Outgoing]
        // 클라이언트가 서버로 메시지를 보낼 때(보내서 컨트롤러로 라우팅할 때) 사용할 주소 접두사입니다.
        // 프론트엔드에서 "/pub/chat/message"로 메시지를 던지면 서버의 @MessageMapping이 이를 낚아챕니다.
        registry.setApplicationDestinationPrefixes("/pub");
    }
}
