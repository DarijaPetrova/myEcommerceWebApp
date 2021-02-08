package com.example.proekt.model;

import com.example.proekt.enumerations.CartStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.jfr.Enabled;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private CartStatus status=CartStatus.CREATED;
    private LocalDateTime createDate=LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id") // ime na nadvoresen kluc
    private User user;

    /*
    @ManyToMany
    @JoinTable(name = "cart_products", joinColumns = @JoinColumn(name="cart_id"), inverseJoinColumns = @JoinColumn(name="product_id"))
    private List<Product> products = new ArrayList<>();

     */

    private BigDecimal grandTotal;
    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CartItem> cartItems;



    public ShoppingCart() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CartStatus getStatus() {
        return status;
    }

    public void setStatus(CartStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /*
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

     */

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }
}


