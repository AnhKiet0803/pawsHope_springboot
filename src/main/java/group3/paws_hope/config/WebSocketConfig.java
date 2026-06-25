package group3.paws_hope.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{

        @Override
        public void configureMessageBroker(MessageBrokerRegistry config) {
            // Kích hoạt broker để gửi thông báo về Client với tiền tố /topic
            config.enableSimpleBroker("/topic");
            config.setApplicationDestinationPrefixes("/app");
        }

        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            // Điểm kết nối dành cho Frontend ReactJS, hỗ trợ SockJS để dự phòng kết nối
            registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
        }
}
