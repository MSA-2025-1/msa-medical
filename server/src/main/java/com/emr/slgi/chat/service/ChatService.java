package com.emr.slgi.chat.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.emr.slgi.chat.DTO.ChatAlarmDTO;
import com.emr.slgi.chat.DTO.ChatMessage;
import com.emr.slgi.chat.DTO.ChatRoom;
import com.emr.slgi.member.domain.Member;
import com.emr.slgi.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class ChatService {
	private final MemberService memberService;
	private final ChatMessageService chatMessageService;
	private final ChatRoomService chatRoomService;
	private final SimpMessageSendingOperations messagingTemplate;
	private final ChatReadService chatReadService;

	public void getUuid(ChatMessage content) {
		
		Member name = memberService.getByUuid(content.getUuid());
		content.setName(name.getName()); // 화면에 표여주기위해서 
	    
		chatMessageService.saveMessage(content);
		messagingTemplate.convertAndSend("/sub/chatroom/" + content.getRoomId(), content);	
	}

	public void getRoomId(ChatMessage content) {
		List<String> uuids = chatRoomService.getUuid(content.getRoomId());
		for(String uu : uuids) {
			 List<ChatRoom> list = chatRoomService.getList(uu);
			 messagingTemplate.convertAndSend("/sub/chatrooms/" + uu, list);
			 List<ChatAlarmDTO> alarmList = chatReadService.getList(uu);
			 messagingTemplate.convertAndSend("/sub/alarms/"+uu,alarmList);
		 }
		
	}

	public void getChatList(ChatRoom load) {
		List<String> uuids = chatRoomService.getUuid(load.getRoomId());
		 for(String uu : uuids) {
			 List<ChatRoom> list = chatRoomService.getList(uu);
			  messagingTemplate.convertAndSend("/sub/chatrooms/" + uu, list);
		 }
	}	
}
