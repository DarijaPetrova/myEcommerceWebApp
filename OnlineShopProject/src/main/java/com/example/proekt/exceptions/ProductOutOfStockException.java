package com.example.proekt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.PRECONDITION_FAILED)
public class ProductOutOfStockException extends RuntimeException{
    public ProductOutOfStockException(String productName){
        super(String.format("Produktot %s go nema na zaliha.",productName));
    }
}
