package com.emr.slgi.status;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Controller
@RequiredArgsConstructor
@Slf4j
public class StatusController {
	private final SimpMessageSendingOperations messagingTemplate;
	private final StatusService statusService;
	
	@MessageMapping("/status/message")
	public void message(Status statusUpdate) {
		System.out.println(">>> 받은 상태: " + statusUpdate); 
		messagingTemplate.convertAndSend("/sub/status",statusUpdate);
		
	}

}



