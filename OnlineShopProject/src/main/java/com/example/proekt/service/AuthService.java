package com.example.proekt.service;

import com.example.proekt.model.User;

public interface AuthService {
    User getCurrentUser(); //go zema momentalno najaveniot korisnik
    String getCurrentUserId();

    User signUpUser(String username, String password, String repeatedPassword);

}
