package com.bd.chitchat.message.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatWsController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void send(ChatSendRequest req, Principal principal) {
        ChatEvent event = new ChatEvent();
        event.setChannelId(req.getChannelId());
        event.setSender(principal.getName()); // comes from CONNECT auth
        event.setContent(req.getContent());
        event.setSentAt(LocalDateTime.now());

        messagingTemplate.convertAndSend("/topic/channels/" + req.getChannelId(), event);
    }

    @Data
    public static class ChatSendRequest {
        private Long channelId;
        private String content;
    }

    @Data
    public static class ChatEvent {
        private Long channelId;
        private String sender;
        private String content;
        private LocalDateTime sentAt;
    }
}
