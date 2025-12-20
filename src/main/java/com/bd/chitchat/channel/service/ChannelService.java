package com.bd.chitchat.channel.service;

import com.bd.chitchat.channel.entity.Channel;
import com.bd.chitchat.channel.entity.ChannelMember;
import com.bd.chitchat.channel.repository.ChannelMemberRepository;
import com.bd.chitchat.channel.repository.ChannelRepository;
import com.bd.chitchat.department.entity.Department;
import com.bd.chitchat.department.repository.DepartmentRepository;
import com.bd.chitchat.user.entity.User;
import com.bd.chitchat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final ChannelMemberRepository channelMemberRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    @Transactional
    public Channel createDepartmentChannel(Long departmentId, String name) {
        Department dept = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Channel channel = new Channel();
        channel.setType(Channel.Type.DEPARTMENT);
        channel.setName(name);
        channel.setDepartment(dept);

        channel = channelRepository.save(channel);

        // Optionally: auto-add all department users as members later

        return channel;
    }

    @Transactional
    public Channel createDmChannel(Long userId1, Long userId2) {
        if (userId1.equals(userId2)) {
            throw new RuntimeException("DM must be between two different users");
        }

        User u1 = userRepository.findById(userId1)
                .orElseThrow(() -> new RuntimeException("User1 not found"));
        User u2 = userRepository.findById(userId2)
                .orElseThrow(() -> new RuntimeException("User2 not found"));

        Channel channel = new Channel();
        channel.setType(Channel.Type.DM);
        channel = channelRepository.save(channel);

        addMemberInternal(channel, u1);
        addMemberInternal(channel, u2);

        return channel;
    }

    @Transactional
    public void addMember(Long channelId, Long userId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        addMemberInternal(channel, user);
    }

    public List<Channel> findAllForUser(Long userId) {
        // simplest: later you can write a query join; for now use member repo
        return channelMemberRepository.findAll().stream()
                .filter(cm -> cm.getUser().getId().equals(userId))
                .map(ChannelMember::getChannel)
                .distinct()
                .toList();
    }

    private void addMemberInternal(Channel channel, User user) {
        if (channelMemberRepository.existsByChannelIdAndUserId(channel.getId(), user.getId())) {
            return;
        }
        ChannelMember cm = new ChannelMember();
        cm.setChannel(channel);
        cm.setUser(user);
        channelMemberRepository.save(cm);
    }
}
