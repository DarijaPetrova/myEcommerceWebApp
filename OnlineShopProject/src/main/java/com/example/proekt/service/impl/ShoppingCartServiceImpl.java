package com.example.proekt.service.impl;

import com.example.proekt.dto.ChargeRequest;
import com.example.proekt.enumerations.CartStatus;
import com.example.proekt.exceptions.*;
import com.example.proekt.model.CartItem;
import com.example.proekt.model.Product;
import com.example.proekt.model.ShoppingCart;
import com.example.proekt.model.User;
import com.example.proekt.repository.CartItemRepository;
import com.example.proekt.repository.ShoppingCartRepository;
import com.example.proekt.service.PaymentService;
import com.example.proekt.service.ProductService;
import com.example.proekt.service.ShoppingCartService;
import com.example.proekt.service.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private UserService userService;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public ShoppingCart createNewShoppingCart(String userId) {
      //proveruva dali postoi User-ot
        User user=this.userService.findById(userId);

        //proveruva dali user-ot ima momentalno kreirana shopping cart, ako ima frla isklucok
        if(this.shoppingCartRepository.existsByUserUsernameAndStatus(user.getUsername(), CartStatus.CREATED)){
            throw new ShoppingCartIsAlreadyCreatedException(userId);
        }
        //ako nema kreirano shopping cart kerira tuka
        ShoppingCart shoppingCart=new ShoppingCart();
        shoppingCart.setUser(user);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    /*@Override
    @Transactional
    public ShoppingCart addProductToShoppingCart(String userId, Long productId) {

        //ja zema kreiranata ili aktivnata shopping cart za toj korisnik
        ShoppingCart shoppingCart = this.getActiveShoppingCart(userId);
        Product product = this.productService.findById(productId);
        //go dodavame ovoj del koga so ovoj service rabotime so view controller
        for (Product p : shoppingCart.getProducts()) {
            if (p.getId().equals(productId)) {
                throw new ProductIsAlreadyInShoppingCartException(product.getName());
            }
        }
        if (shoppingCart.getProducts() == null) {
            shoppingCart.setProducts(new ArrayList<>());
        }

        shoppingCart.getProducts().add(product);
        return this.shoppingCartRepository.save(shoppingCart);
    }*/


    @Override
    public ShoppingCart getActiveShoppingCart(String userId) {
        //ovaa funkcja ja vraka aktivnata shopping cart, a dokolku nema kreira
        return this.shoppingCartRepository.findByUserUsernameAndStatus(userId,CartStatus.CREATED)
                .orElseGet(()->{
                    ShoppingCart shoppingCart=new ShoppingCart();
                    User user=this.userService.findById(userId);
                    shoppingCart.setUser(user);
                    return this.shoppingCartRepository.save(shoppingCart);
                });
    }

    /*@Override
    @Transactional
    public ShoppingCart removeProductFromShoppingCart(String userId, Long productId) {
        ShoppingCart shoppingCart = this.getActiveShoppingCart(userId);
        shoppingCart.setProducts(
                shoppingCart.getProducts()
                .stream()
                .filter(product -> !product.getId().equals(productId))
                .collect(Collectors.toList())
        );
        return this.shoppingCartRepository.save(shoppingCart);
    }*/

    @Override
    public ShoppingCart cancelActiveShoppingCart(String userId) {
        ShoppingCart shoppingCart = this.shoppingCartRepository
                .findByUserUsernameAndStatus(userId,CartStatus.CREATED)
                .orElseThrow(()->new ShoppingCartIsNotActiveException(userId));
                shoppingCart.setStatus(CartStatus.CANCELED);

                return this.shoppingCartRepository.save(shoppingCart);


    }

    @Override
    @Transactional
    public ShoppingCart checkoutShoppingCart(String userId, ChargeRequest chargeRequest) {
       ShoppingCart shoppingCart = this.shoppingCartRepository
                .findByUserUsernameAndStatus(userId, CartStatus.CREATED)
                .orElseThrow(() -> new ShoppingCartIsNotActiveException(userId));

       // List<Product> products = shoppingCart.getProducts();
        List<CartItem> cartItems = shoppingCart.getCartItems();

        float price = 0;
        for (CartItem cartItem : cartItems) {
            if (cartItem.getProduct().getQuantity() <= 0) {
                throw new ProductOutOfStockException(cartItem.getProduct().getName());
            }
            cartItem.getProduct().setQuantity(cartItem.getProduct().getQuantity() - 1);
            price += cartItem.getSubtotal();
        }
        //this.paymentService.pay(price);
      Charge charge = null;
        try {
           charge = this.paymentService.pay(chargeRequest);
        } catch (StripeException e) {
            throw new TransactionFailedException(userId, e.getMessage());
        }
       // shoppingCart.setProducts(products);
        shoppingCart.setCartItems(cartItems);
        shoppingCart.setStatus(CartStatus.FINISHED);
            return this.shoppingCartRepository.save(shoppingCart);


    }

    @Override
    public ShoppingCart findActiveShoppingCartByUsername(String userId) {
        return this.shoppingCartRepository.findByUserUsernameAndStatus(userId,CartStatus.CREATED)
                .orElseThrow(()->new ShoppingCartIsNotActiveException(userId));
    }

    @Override
    @Transactional
    public ShoppingCart addCartItemToShoppingCart(String userId, Long productId) {
        //ja zema kreiranata ili aktivnata shopping cart za toj korisnik
        ShoppingCart shoppingCart = this.getActiveShoppingCart(userId);
        Product product=this.productService.findById(productId);


        CartItem cartItem=this.cartItemRepository.findByProductIdAndShoppingCartId(product.getId(), shoppingCart.getId());

        if(cartItem==null) {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            if (cartItem.getProduct().getQuantity() == 0) {
                throw new ProductOutOfStockException(cartItem.getProduct().getName());
            }

            cartItem.setQuantity(1);
            cartItem.setSubtotal(cartItem.getProduct().getPrice() * cartItem.getQuantity());
            cartItem.setShoppingCart(shoppingCart);
            shoppingCart.getCartItems().add(cartItem);
        }else {
            if(cartItem.getProduct().getQuantity()==cartItem.getQuantity())
            {
                throw new ProductOutOfStockException(cartItem.getProduct().getName());
            }
            else if(cartItem.getProduct().getQuantity()==0){
                throw new ProductOutOfStockException(cartItem.getProduct().getName());
            }
            else {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                cartItem.setSubtotal(cartItem.getProduct().getPrice() * cartItem.getQuantity());
                shoppingCart.getCartItems().add(cartItem);
            }
        }

        if (shoppingCart.getCartItems() == null) {
            shoppingCart.setCartItems(new ArrayList<>());
        }


        //System.out.println(cartItem.getProduct().getName());


        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCart removeCartItemFromShoppingCart(String userId, Long productId) {
        ShoppingCart shoppingCart = this.getActiveShoppingCart(userId);
        Product product=this.productService.findById(productId);

        CartItem cartItem=this.cartItemRepository.findByProductIdAndShoppingCartId(product.getId(), shoppingCart.getId());
        if(cartItem!=null){
            if(cartItem.getQuantity()>1){
                cartItem.setQuantity(cartItem.getQuantity()-1);
                cartItem.setSubtotal(cartItem.getSubtotal()-cartItem.getProduct().getPrice());
            }
            else {
                this.cartItemRepository.delete(cartItem);

            }
        }
        else {
            throw new ProductIsNotInShoppingCartException();
        }
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public void deleteCartItemFromShoppingCart(String userId, Long productId) {
        ShoppingCart shoppingCart = this.getActiveShoppingCart(userId);
        Product product=this.productService.findById(productId);

        CartItem cartItem=this.cartItemRepository.findByProductIdAndShoppingCartId(product.getId(), shoppingCart.getId());
        this.cartItemRepository.delete(cartItem);
    }
}
