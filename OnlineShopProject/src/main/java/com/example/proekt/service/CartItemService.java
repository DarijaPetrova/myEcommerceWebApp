package com.example.proekt.service;

import com.example.proekt.model.CartItem;
import com.example.proekt.model.Product;

public interface CartItemService {
CartItem increaseQuantity(CartItem cartItem);
CartItem decreaseQuantity(CartItem cartItem);
}
