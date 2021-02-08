package com.example.proekt.service.impl;


import com.example.proekt.model.CartItem;
import com.example.proekt.service.CartItemService;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImpl implements CartItemService {


    @Override
    public CartItem increaseQuantity(CartItem cartItem) {
        cartItem.setQuantity(cartItem.getQuantity()+1);
        return cartItem;
    }

    @Override
    public CartItem decreaseQuantity(CartItem cartItem) {
        cartItem.setQuantity(cartItem.getQuantity()-1);
        return cartItem;
    }
}
