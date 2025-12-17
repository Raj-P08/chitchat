package com.bd.chitchat.department.entity;

import com.bd.chitchat.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_departments",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "department_id"}))
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(nullable = false)
    private LocalDateTime joinedAt = LocalDateTime.now();
}
