package com.example.proekt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String username) {
        super(String.format("User with username: %s already exists!", username));
    }
}
