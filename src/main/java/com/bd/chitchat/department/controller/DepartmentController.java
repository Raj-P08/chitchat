package com.bd.chitchat.department.controller;

import com.bd.chitchat.department.entity.Department;
import com.bd.chitchat.department.service.DepartmentService;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<Department>> list() {
        return ResponseEntity.ok(departmentService.getAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Department> create(@RequestBody CreateDepartmentRequest req) {
        return ResponseEntity.ok(departmentService.create(req.getName(), req.getDescription()));
    }

    @PostMapping("/{departmentId}/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> addUser(@PathVariable Long departmentId, @PathVariable Long userId) {
        departmentService.addUserToDepartment(userId, departmentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{departmentId}/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeUser(@PathVariable Long departmentId, @PathVariable Long userId) {
        departmentService.removeUserFromDepartment(userId, departmentId);
        return ResponseEntity.noContent().build();
    }

    @Data
    public static class CreateDepartmentRequest {
        @NotBlank
        private String name;
        private String description;
    }
}
