package com.example.proekt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
public class ProductIsNotInShoppingCartException extends RuntimeException {
    public ProductIsNotInShoppingCartException(){
        super(String.format("Produktot ne e dodaden vo shopingcart."));
    }
}
