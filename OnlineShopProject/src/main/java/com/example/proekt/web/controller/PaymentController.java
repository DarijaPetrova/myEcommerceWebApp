package com.example.proekt.web.controller;

import com.example.proekt.dto.ChargeRequest;
import com.example.proekt.model.ShoppingCart;
import com.example.proekt.service.AuthService;
import com.example.proekt.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payments")
public class PaymentController {
    @Value("${STRIPE_P_KEY}")
    private String publicKey;
    private final ShoppingCartService shoppingCartService;
    private final AuthService authService;

    public PaymentController(ShoppingCartService shoppingCartService, AuthService authService) {
        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
    }

    @GetMapping("/charge")
    public String getCheckoutPage(Model model) {
        //za najaveniot korisnik da se zeme aktivnata shopping cart
        try {
            ShoppingCart shoppingCart = this.shoppingCartService.findActiveShoppingCartByUsername(this.authService.getCurrentUserId());
            model.addAttribute("shoppingCart", shoppingCart);
            model.addAttribute("currency", "MKD");
           // model.addAttribute("amount", (int) (shoppingCart.getProducts().stream().mapToDouble(product -> product.getPrice()).sum() * 100));
            model.addAttribute("amount", (int) (shoppingCart.getCartItems().stream().mapToDouble(cartItem -> cartItem.getSubtotal()).sum() * 100));
            model.addAttribute("stripePublicKey", this.publicKey);
        } catch (RuntimeException ex) {
            return "redirect:/products?error= " + ex.getLocalizedMessage();
        }

        return "shoppingcart";
    }

    @PostMapping("/charge")
    public String checkout(ChargeRequest chargeRequest) {
        try {
            ShoppingCart shoppingCart = this.shoppingCartService.checkoutShoppingCart(this.authService.getCurrentUserId(), chargeRequest);

            return "redirect:/products?message= Uspesno plakanje.";
        } catch (RuntimeException ex) {
            return "redirect:/shoppingcart-payments/charge?error= " + ex.getLocalizedMessage();
        }

    }


}

