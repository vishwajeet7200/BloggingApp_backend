package com.blog.app.services;

import java.util.List;

import com.blog.app.dtos.UserDTO;
import com.blog.app.models.User;

public interface UserService {
    void updateUserRole(Long userId, String roleName);

    List<User> getAllUsers();

    UserDTO getUserById(Long id);

	User findByUsername(String username);
    
    
}
