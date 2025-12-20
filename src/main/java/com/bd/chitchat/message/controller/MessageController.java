package com.bd.chitchat.message.controller;

import com.bd.chitchat.channel.repository.ChannelMemberRepository;
import com.bd.chitchat.message.entity.Message;
import com.bd.chitchat.message.repository.MessageRepository;
import com.bd.chitchat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class MessageController {

    private final MessageRepository messageRepository;
    private final ChannelMemberRepository channelMemberRepository;
    private final UserRepository userRepository;

    @GetMapping("/{channelId}/messages")
    public ResponseEntity<Page<Message>> history(
            @PathVariable Long channelId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            Authentication auth
    ) {
        var user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!channelMemberRepository.existsByChannelIdAndUserId(channelId, user.getId())) {
            return ResponseEntity.status(403).build();
        }

        Page<Message> result = messageRepository
                .findByChannelIdOrderByCreatedAtDesc(channelId, PageRequest.of(page, size));

        return ResponseEntity.ok(result);
    }
}
