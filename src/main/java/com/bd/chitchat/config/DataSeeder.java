package com.bd.chitchat.config;

import com.bd.chitchat.user.entity.Role;
import com.bd.chitchat.user.entity.User;
import com.bd.chitchat.user.repository.RoleRepository;
import com.bd.chitchat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Seed roles if they don't exist
        if (roleRepository.count() == 0) {
            Role adminRole = new Role(null, "ADMIN");
            Role managerRole = new Role(null, "MANAGER");
            Role employeeRole = new Role(null, "EMPLOYEE");
            
            roleRepository.save(adminRole);
            roleRepository.save(managerRole);
            roleRepository.save(employeeRole);
            
            System.out.println("✅ Roles seeded: ADMIN, MANAGER, EMPLOYEE");
        }
        
        // Seed default admin user if no users exist
        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("ADMIN role not found"));
            
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(PasswordEncoder.encode("admin123")); // Will hash with BCrypt later
            admin.setDisplayName("System Administrator");
            admin.setEmail("admin@chitchat.com");
            admin.setRoles(Set.of(adminRole));
            admin.setActive(true);
            
            userRepository.save(admin);
            System.out.println("✅ Default admin user created: username=admin, password=admin123");
        }
    }
}
