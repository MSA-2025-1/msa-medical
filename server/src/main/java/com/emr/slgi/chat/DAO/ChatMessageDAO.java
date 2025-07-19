package com.emr.slgi.chat.DAO;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.emr.slgi.chat.DTO.ChatMessage;

@Mapper
public interface ChatMessageDAO {

	public List<ChatMessage> getList(String roomId);

	public void saveMessage(ChatMessage content);

	public ChatMessage getById(int messageId);
}