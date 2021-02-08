package com.example.proekt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
public class ProductIsAlreadyInShoppingCartException extends RuntimeException{
    public ProductIsAlreadyInShoppingCartException(String productName){
        super(String.format("Produktot e veke vo shopping cart i ne moze da se izbrishe.",productName));
    }
}
