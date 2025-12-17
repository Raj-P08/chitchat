package com.bd.chitchat.department.service;

import com.bd.chitchat.department.entity.Department;
import com.bd.chitchat.department.entity.UserDepartment;
import com.bd.chitchat.department.repository.DepartmentRepository;
import com.bd.chitchat.department.repository.UserDepartmentRepository;
import com.bd.chitchat.user.entity.User;
import com.bd.chitchat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UserDepartmentRepository userDepartmentRepository;
    private final UserRepository userRepository;

    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    @Transactional
    public Department create(String name, String description) {
        if (departmentRepository.existsByName(name)) {
            throw new RuntimeException("Department name already exists");
        }
        Department d = new Department();
        d.setName(name);
        d.setDescription(description);
        return departmentRepository.save(d);
    }

    @Transactional
    public void addUserToDepartment(Long userId, Long departmentId) {
        if (userDepartmentRepository.existsByUserIdAndDepartmentId(userId, departmentId)) return;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Department dept = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        UserDepartment ud = new UserDepartment();
        ud.setUser(user);
        ud.setDepartment(dept);
        userDepartmentRepository.save(ud);
    }

    @Transactional
    public void removeUserFromDepartment(Long userId, Long departmentId) {
        userDepartmentRepository.deleteByUserIdAndDepartmentId(userId, departmentId);
    }
}
