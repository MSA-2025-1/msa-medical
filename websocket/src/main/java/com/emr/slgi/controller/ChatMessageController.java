package com.emr.slgi.message;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatRestMessageController {
	private final ChatMessageService chatMessageService;
	
	@GetMapping("/list/{roomId}")
	public ResponseEntity<Object> list(@PathVariable("roomId") String roomId) {
		List<ChatMessage> list = chatMessageService.getList(roomId);
		return ResponseEntity.ok(list);
		
	}
}
