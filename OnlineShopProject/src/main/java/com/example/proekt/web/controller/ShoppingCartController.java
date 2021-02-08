package com.example.proekt.web.controller;

import com.example.proekt.model.CartItem;
import com.example.proekt.model.Product;
import com.example.proekt.model.ShoppingCart;
import com.example.proekt.repository.CartItemRepository;
import com.example.proekt.service.AuthService;
import com.example.proekt.service.CartItemService;
import com.example.proekt.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private AuthService authService;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private CartItemRepository cartItemRepository;

   /* @GetMapping
    public String getShoppingCartPage(Model model){
        ShoppingCart shoppingCart=this.shoppingCartService.getActiveShoppingCart(this.authService.getCurrentUser().getUsername());
        List<Product> products = shoppingCart.getProducts();
        model.addAttribute("productsInShoppingCart", products);

        return "shoppingcart";
    }*/

    @PostMapping("/{productId}/add-product")
    public String addProductToShoppingCart(@PathVariable Long productId){
        try {
            ShoppingCart shoppingCart = this.shoppingCartService.addCartItemToShoppingCart(this.authService.getCurrentUserId(), productId);

        } catch (RuntimeException ex) {
            return "redirect:/products?error=" + ex.getLocalizedMessage();
        }
        return "redirect:/products";
    }

    @PostMapping("/{productId}/remove-product")
    public String removeProductFromShoppingCart(@PathVariable Long productId){
        try {
            ShoppingCart shoppingCart = this.shoppingCartService.removeCartItemFromShoppingCart(this.authService.getCurrentUserId(), productId);
        }
        catch (RuntimeException ex) {
            return "redirect:/products?error=" + ex.getLocalizedMessage();
        }
            return  "redirect:/products";

    }
    @PostMapping("/{productId}/increaseQuantity")
    public String increaseQuantity(@PathVariable Long productId){
        try {
            ShoppingCart shoppingCart = this.shoppingCartService.addCartItemToShoppingCart(this.authService.getCurrentUserId(), productId);
        }
        catch  (RuntimeException ex) {
            return  "redirect:/payments/charge?error=" + ex.getLocalizedMessage();
        }
        return  "redirect:/payments/charge";
    }

    @PostMapping("/{productId}/decreaseQuantity")
    public String decreaseQuantity(@PathVariable Long productId){
        try {
            ShoppingCart shoppingCart = this.shoppingCartService.removeCartItemFromShoppingCart(this.authService.getCurrentUserId(), productId);
        }
        catch  (RuntimeException ex) {
            return  "redirect:/payments/charge?error=" + ex.getLocalizedMessage();
        }
        return  "redirect:/payments/charge";
    }
    @PostMapping("/cancel")
    public String cancelShoppingCart(){
        this.shoppingCartService.cancelActiveShoppingCart(this.authService.getCurrentUserId());
        return  "redirect:/products";
    }

    /*@PostMapping("/checkout")
    public String chackoutShoppingCart(){
        this.shoppingCartService.checkoutShoppingCart(this.authService.getCurrentUser().getUsername());
        return  "redirect:/products";
    }*/

    @PostMapping("/{productId}/delete")
    public String deleteCartItemFromShoppingCart(@PathVariable Long productId){
         this.shoppingCartService.deleteCartItemFromShoppingCart(this.authService.getCurrentUserId(),productId);
        return  "redirect:/payments/charge";

    }



}
