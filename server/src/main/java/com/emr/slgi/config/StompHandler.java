package com.emr.slgi.config;

import java.util.Map.Entry;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StompHandler implements ChannelInterceptor{
	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel){
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		for (Entry<String, Object> entry : message.getHeaders().entrySet()) {
	          log.info("preSend() header -> {}", entry); 
	        }
		
		if (StompCommand.CONNECT == accessor.getCommand()) {
			
		    String sender = accessor.getFirstNativeHeader("sender");
		    String token = accessor.getFirstNativeHeader("token");

		    if (token == null || token.isBlank()) {
		        throw new IllegalArgumentException("로그인된 사용자만 접속 가능합니다.");
		    }

		    log.info("CONNECT 요청한 사용자 = {}", sender);
		    log.info("CONNECT 요청한 사용자 = {}", token);
		}
		return message;
	}
	

}