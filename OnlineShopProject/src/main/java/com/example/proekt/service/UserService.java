package com.example.proekt.service;

import com.example.proekt.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findById(String userId);
    User createUser(String username, String password, String repeatedPassword);
    User registerUser(User user);
}
