package com.bd.chitchat.user.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.bd.chitchat.user.entity.Role;
import com.bd.chitchat.user.entity.User;
import com.bd.chitchat.user.repository.RoleRepository;
import com.bd.chitchat.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	
	public List<User> getAllUsers(){
		return userRepository.findAll();
	}
	
	public Optional<User> getUserById(Long id){
		return userRepository.findById(id);
	}
	
	
	public Optional<User> getUserByUsername(String username){
		return userRepository.findByUsername(username);
	}
	
	@Transactional
    public User createUser(String username, String password, String displayName, 
                          String email, Set<Role> roles) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // Will hash later with BCrypt
        user.setDisplayName(displayName);
        user.setEmail(email);
        user.setRoles(roles);
        user.setActive(true);
        return userRepository.save(user);
    }
    
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

	
	
	

}
