package com.blog.app.services.Impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.app.dtos.UserDTO;
import com.blog.app.models.AppRole;
import com.blog.app.models.Role;
import com.blog.app.models.User;
import com.blog.app.repositories.RoleRepository;
import com.blog.app.repositories.UserRepository;
import com.blog.app.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Override
	public void updateUserRole(Long userId, String roleName) {
		User user=userRepository.findById(userId).orElseThrow(()
				-> new RuntimeException("User not found"));
		AppRole appRole=AppRole.valueOf(roleName);
		Role role=roleRepository.findByRoleName(appRole).orElseThrow(() 
				-> new RuntimeException("Role not found"));
		user.setRole(role);
		userRepository.save(user);
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public UserDTO getUserById(Long id) {
		
		User user=userRepository.findById(id).orElseThrow(()
				-> new RuntimeException("User not found"));
		
		return convertToDto(user);
	}

	private UserDTO convertToDto(User user) {
		UserDTO userDTO=new UserDTO();
		BeanUtils.copyProperties(user, userDTO);
		return userDTO;
	}
	
	@Override
    public User findByUsername(String username) {
        Optional<User> user = userRepository.findByUserName(username);
        return user.orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

}
