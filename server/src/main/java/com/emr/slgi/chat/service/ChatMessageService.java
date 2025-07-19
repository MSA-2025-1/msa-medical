package com.emr.slgi.chat.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.emr.slgi.chat.DAO.ChatMessageDAO;
import com.emr.slgi.chat.DTO.ChatMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
	private final ChatMessageDAO chatMessageDAO;

	public List<ChatMessage> getList(String roomId) {
		return chatMessageDAO.getList(roomId);
	}
	
	
	public void saveMessage(ChatMessage content) {
		chatMessageDAO.saveMessage(content);
		
		
	}


	public ChatMessage getById(int messageId) {
		return chatMessageDAO.getById(messageId);
	}

}