package com.bd.chitchat.channel.entity;

import com.bd.chitchat.department.entity.Department;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "channels")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Channel {

    public enum Type { DEPARTMENT, DM }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Type type;

    @Column(length = 120)
    private String name; // for department channels

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department; // only for DEPARTMENT type
}
