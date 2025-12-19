package com.bd.chitchat.channel.repository;

import com.bd.chitchat.channel.entity.ChannelMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember, Long> {
    boolean existsByChannelIdAndUserId(Long channelId, Long userId);
}
