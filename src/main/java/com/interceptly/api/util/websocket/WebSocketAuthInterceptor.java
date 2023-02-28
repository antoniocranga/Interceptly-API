package com.interceptly.api.util.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    @Autowired
    JwtDecoder jwtDecoder;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final var accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        final var cmd = accessor.getCommand();
        String token = null;
        if (StompCommand.CONNECT == cmd || StompCommand.SEND == cmd) {
            final var requestTokenHeader = accessor.getFirstNativeHeader("Authorization");
            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer")) {
                token = requestTokenHeader.substring(7);
            }
            log.info(token);
            Jwt jwt = jwtDecoder.decode(token);
            JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
            Authentication authentication = converter.convert(jwt);
            accessor.setUser(authentication);
        }
        return message;
    }

}


