package com.bd.chitchat.message.service;

import com.bd.chitchat.channel.entity.Channel;
import com.bd.chitchat.channel.repository.ChannelMemberRepository;
import com.bd.chitchat.channel.repository.ChannelRepository;
import com.bd.chitchat.message.entity.Message;
import com.bd.chitchat.message.repository.MessageRepository;
import com.bd.chitchat.user.entity.User;
import com.bd.chitchat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final ChannelMemberRepository channelMemberRepository;
    private final UserRepository userRepository;

    @Transactional
    public Message saveMessage(Long channelId, String username, String content) {
        User sender = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        if (!channelMemberRepository.existsByChannelIdAndUserId(channelId, sender.getId())) {
            throw new RuntimeException("Not a member of this channel");
        }

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));

        Message m = new Message();
        m.setChannel(channel);
        m.setSender(sender);
        m.setContent(content);

        return messageRepository.save(m);
    }
}
