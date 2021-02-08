package com.example.proekt.service;

import com.example.proekt.dto.ChargeRequest;
import com.example.proekt.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart createNewShoppingCart(String userId);
    //ShoppingCart addProductToShoppingCart(String userId, Long productId);
    ShoppingCart getActiveShoppingCart(String userId);
    //ShoppingCart removeProductFromShoppingCart(String userId, Long productId);
    ShoppingCart cancelActiveShoppingCart(String userId);
    ShoppingCart checkoutShoppingCart (String userId, ChargeRequest chargeRequest);
    ShoppingCart findActiveShoppingCartByUsername(String userId);

    ShoppingCart addCartItemToShoppingCart(String userId, Long productId);
    ShoppingCart removeCartItemFromShoppingCart(String userId, Long productId);

    void deleteCartItemFromShoppingCart(String userId, Long productId);
}
