package com.example.proekt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
public class ShoppingCartIsAlreadyCreatedException  extends RuntimeException{
    public ShoppingCartIsAlreadyCreatedException(String userId) {
        super(String.format("Shopping cart is already created for user: %s", userId));
    }
}
