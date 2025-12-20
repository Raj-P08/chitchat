package com.bd.chitchat.message.controller;

import com.bd.chitchat.message.entity.Message;
import com.bd.chitchat.message.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatWsController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    @MessageMapping("/chat.send")
    public void send(ChatSendRequest req, Principal principal) {
        Message msg = messageService.saveMessage(req.getChannelId(), principal.getName(), req.getContent());

        ChatMessageDto dto = new ChatMessageDto(
                msg.getId(),
                msg.getChannel().getId(),
                msg.getSender().getUsername(),
                msg.getContent(),
                msg.getCreatedAt()
        );

        messagingTemplate.convertAndSend("/topic/channels/" + req.getChannelId(), dto);
    }

    @Data
    public static class ChatSendRequest {
        private Long channelId;
        private String content;
    }

    @Data
    @AllArgsConstructor
    public static class ChatMessageDto {
        private Long id;
        private Long channelId;
        private String senderUsername;
        private String content;
        private java.time.LocalDateTime createdAt;
    }
}
