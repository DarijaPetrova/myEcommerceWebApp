package com.example.proekt.service.impl;

import com.example.proekt.exceptions.UserAlreadyExistException;
import com.example.proekt.exceptions.UserNotFoundException;
import com.example.proekt.model.Role;
import com.example.proekt.model.User;
import com.example.proekt.repository.RoleRepository;
import com.example.proekt.repository.UserRepository;
import com.example.proekt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User findById(String userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException(userId));
    }

    @Override
    public User createUser(String username, String password, String repeatedPassword) {
        User user=new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        Role userRole=this.roleRepository.findByName("ROLE_USER");
        user.setRoles(Collections.singletonList(userRole));
        return user;
    }

    @Override
    public User registerUser(User user) {
        if(this.userRepository.existsById(user.getUsername())){
            throw new UserAlreadyExistException(user.getUsername());
        }
        return this.userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return this.userRepository.findById(s)
                .orElseThrow(()->new UserNotFoundException(s));

    }
}
