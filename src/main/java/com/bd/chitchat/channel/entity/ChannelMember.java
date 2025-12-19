package com.bd.chitchat.channel.entity;

import com.bd.chitchat.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
  name = "channel_members",
  uniqueConstraints = @UniqueConstraint(columnNames = {"channel_id","user_id"})
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ChannelMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
