package com.example.proekt.service.impl;

import com.example.proekt.exceptions.PasswordDoesntMatchException;
import com.example.proekt.exceptions.UserNotFoundException;
import com.example.proekt.model.Role;
import com.example.proekt.model.User;
import com.example.proekt.repository.RoleRepository;
import com.example.proekt.repository.UserRepository;
import com.example.proekt.service.AuthService;
import com.example.proekt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    //kreiranje na admin dokolku ne postoi
    @PostConstruct
    public void init(){
        if (!this.userRepository.existsById("admin")){
            User admin=new User();
            admin.setUsername("admin");
            admin.setPassword(this.passwordEncoder.encode("admin"));
            admin.setRoles(this.roleRepository.findAll());

           // Role userRole=this.roleRepository.findByName("ROLE_ADMIN");
            //admin.setRoles(Collections.singletonList(userRole));
            this.userRepository.save(admin);
        }
    }

    @Override
    public User getCurrentUser() {
        /*return this.userRepository.findById("emt-user")
                .orElseGet(()->{
                    User user = new User();
                    user.setUsername("emt-user");
                    return this.userRepository.save(user);
                });*/
        //za da go vraka momentalno korisnikot sto e najaven
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public String getCurrentUserId() {
        return this.getCurrentUser().getUsername();
    }


    @Override
    public User signUpUser(String username, String password, String repeatedPassword) {
        if(!password.equals(repeatedPassword)){
            throw new PasswordDoesntMatchException();
        }

        /*User user=new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        Role userRole=this.roleRepository.findByName("ROLE_USER");
        user.setRoles(Collections.singletonList(userRole));*/

        User user=this.userService.createUser(username,password,repeatedPassword);

        return this.userService.registerUser(user);
    }
}
