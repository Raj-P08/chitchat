package com.bd.chitchat.department.repository;

import com.bd.chitchat.department.entity.UserDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDepartmentRepository extends JpaRepository<UserDepartment, Long> {
    List<UserDepartment> findByUserId(Long userId);
    List<UserDepartment> findByDepartmentId(Long departmentId);
    boolean existsByUserIdAndDepartmentId(Long userId, Long departmentId);
    void deleteByUserIdAndDepartmentId(Long userId, Long departmentId);
}
