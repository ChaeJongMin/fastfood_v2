package com.example.demo.config.WebSocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
//메시지 브로커가 메시지를 처리할 수 있게 활성화
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class ChatConfig implements WebSocketMessageBrokerConfigurer {


    //소켓에 연결하기 위한 엔드 포인트를 지정하는 메소드이며 지정된 경로(엔드포인트)로 프론트앤드 측에서 Stomp를 연결 시도를 하면된다.
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // /stomp/chat 경로를 엔드포인트로 설정
        // CORS를 피하기 위해 setAllowedOriginPatterns을 * 으로 설정
        // addInterceptors는 애플리케이션 내에 인터셉터를 등록해 주는 메소드이며 HttpSessionHandshakeInterceptor를 등록
        // withSockJS() 메서드는 SockJS fallback 옵션을 활성화하여 WebSocket을 사용할 수 없는 경우 대체 메세징 옵션을 사용할 수 있도록 한다.
        registry.addEndpoint("/stomp/chat").setAllowedOriginPatterns("*").addInterceptors(new HttpSessionHandshakeInterceptor()).withSockJS();
    }

    /*
     메시지 브로커를 구성하는 메서드로 유저가 메세지를 전송하거나 받을 수 있도록 중간에서 URL prefix(접두어)를 인식하여
     올바르게 전송(publish), 전달(subscribe)을 중개해주는 중개자(Broker) 역할하도록 해주는 메소드
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트가 STOMP를 사용하여 서버로 메시지를 발송할 수 있는 경로의 prefix(/send)를 지정
        // 이 떄 서버에서 메세지를 처리하는 메서드(message-handling methods)로 라우팅 된다.
        registry.setApplicationDestinationPrefixes("/send");
        // 메시지 브로커가 /receive 으로 시작하는 주소를 구독한 Subscriber들에게 메시지를 전달
        registry.enableSimpleBroker("/receive");
    }

}
