package com.example.proekt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
//Da bide 400 i nesto klientska greska
public class PasswordDoesntMatchException extends RuntimeException {
    public PasswordDoesntMatchException() {
        super("Password doesn't match!");
    }
}
