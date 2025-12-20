package com.bd.chitchat.channel.controller;

import com.bd.chitchat.channel.entity.Channel;
import com.bd.chitchat.channel.service.ChannelService;
import com.bd.chitchat.user.entity.User;
import com.bd.chitchat.user.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;
    private final UserRepository userRepository;

    @PostMapping("/department")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Channel> createDepartmentChannel(@RequestBody CreateDeptChannelRequest req) {
        Channel c = channelService.createDepartmentChannel(req.getDepartmentId(), req.getName());
        return ResponseEntity.ok(c);
    }

    @PostMapping("/dm")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public ResponseEntity<Channel> createDmChannel(@RequestBody CreateDmChannelRequest req) {
        Channel c = channelService.createDmChannel(req.getUserId1(), req.getUserId2());
        return ResponseEntity.ok(c);
    }

    @PostMapping("/{channelId}/members/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> addMember(@PathVariable Long channelId, @PathVariable Long userId) {
        channelService.addMember(channelId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<List<Channel>> myChannels(Authentication auth) {
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(channelService.findAllForUser(user.getId()));
    }

    @Data
    public static class CreateDeptChannelRequest {
        @NotNull
        private Long departmentId;
        private String name;
    }

    @Data
    public static class CreateDmChannelRequest {
        @NotNull
        private Long userId1;
        @NotNull
        private Long userId2;
    }
}
