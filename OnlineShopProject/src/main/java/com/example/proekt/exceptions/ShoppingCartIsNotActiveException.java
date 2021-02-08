package com.example.proekt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ShoppingCartIsNotActiveException  extends  RuntimeException{
    public ShoppingCartIsNotActiveException(String userId){
        super(String.format("Nema aktivna shopping cart za korisnik: %s",userId));
    }
}
