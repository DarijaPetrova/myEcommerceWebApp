package com.example.proekt.web.controller;

import com.example.proekt.exceptions.ProductIsAlreadyInShoppingCartException;
import com.example.proekt.model.CartItem;
import com.example.proekt.model.Category;
import com.example.proekt.model.Product;
import com.example.proekt.model.ShoppingCart;
import com.example.proekt.repository.CategoryRepository;
import com.example.proekt.repository.ProductRepository;
import com.example.proekt.service.AuthService;
import com.example.proekt.service.ProductService;

import com.example.proekt.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    //zaradi save promeneta so zacuvvuanje na slika, i za delete
    @Autowired
    private ProductService productService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private AuthService authService;
    @GetMapping
    public String getProductPage(Model model){
        List<Product> products=this.productRepository.findAll();
        model.addAttribute("products",products); //za da se prikazuvat produktite na html, odnosno se spojuvaat so model
        //za na /products da gi prikazuva i kategoriite
        List<Category> categories=this.categoryRepository.findAll();
        model.addAttribute("categories",categories);
        //za da pokazuva gore brojce kolku produkti ima vo shoppingcart
        ShoppingCart shoppingCart=this.shoppingCartService.getActiveShoppingCart(this.authService.getCurrentUser().getUsername());
        //List<Product> productsInShoppingCart = shoppingCart.getProducts();
        //List<CartItem> productsInShoppingCart = shoppingCart.getCartItems();
        model.addAttribute("shoppingCart", shoppingCart);

        return "products";
    }



    @GetMapping("/search")
    public String getSearchedProducts(Model model,@RequestParam("keyword") String keyword){
        //List<Product> products=this.productRepository.findAll();
        //model.addAttribute("products",products); //za da se prikazuvat produktite na html, odnosno se spojuvaat so model

        //za na /products da gi prikazuva i kategoriite
        List<Category> categories=this.categoryRepository.findAll();
        model.addAttribute("categories",categories);

        //za da pokazuva gore brojce kolku produkti ima vo shoppingcart
        ShoppingCart shoppingCart=this.shoppingCartService.getActiveShoppingCart(this.authService.getCurrentUser().getUsername());;
        model.addAttribute("shoppingCart", shoppingCart);


        List<Product> products = productService.listAll(keyword);
        model.addAttribute("products", products);
        //model.addAttribute("keyword", keyword);

        /*for (Product p: products) {
            System.out.println(p.getName());
        }*/

        return "products";
    }

    //za pri klik na kopceto dodadi nov produkt da ja vrati stranata add-product.html, zatoa slusa na /add-new, a vo products.html kopceto dodadi nov produkt praka get baranje na /products/add-new
    @GetMapping("/add-new")
    @Secured("ROLE_ADMIN")
    public String addNewProductPage(Model model){
        List<Category> categories=this.categoryRepository.findAll();
        //za pri dodavanje na produkt da moze za nego da se dodava nekoja od prethodno dodadenite kategorii
        model.addAttribute("categories",categories);
        model.addAttribute("product", new Product());

        return "add-product";
    }


    @PostMapping
    @Secured("ROLE_ADMIN")
    public String saveProduct(Product product,
                              BindingResult bindingResult, Model model,
                              @RequestParam MultipartFile image) {

        //se proveruva otkako e dodadena validacija
        if (bindingResult.hasErrors()) {
            //isto gi dodava site proizvoditeli
            List<Category>categories = this.categoryRepository.findAll();
            model.addAttribute("categories", categories);
            return "add-product";
        }
        //za slika
        try {
            this.productService.saveProduct(product, image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/products";
    }
    /*@PostMapping
    public String saveProduct(Product product){
        this.productRepository.save(product);
        return "/products";
    }*/

    @GetMapping("/category/{id}")
    public String getProductPageForThisCategory(@PathVariable Long id,Model model){
        List<Product> products=this.productRepository.findAllByCategoryId(id);
        model.addAttribute("products",products);

        //za na /products da gi prikazuva i kategoriite
        List<Category> categories=this.categoryRepository.findAll();
        model.addAttribute("categories",categories);

        //za da pokazuva gore brojce kolku produkti ima vo shoppingcart

        ShoppingCart shoppingCart=this.shoppingCartService.getActiveShoppingCart(this.authService.getCurrentUser().getUsername());
        //List<Product> productsInShoppingCart = shoppingCart.getProducts();
        //List<CartItem> productsInShoppingCart = shoppingCart.getCartItems();
       // model.addAttribute("productsInShoppingCart", productsInShoppingCart);
        model.addAttribute("shoppingCart", shoppingCart);

        return "products";
    }

    @GetMapping("/{id}/edit")
    @Secured("ROLE_ADMIN")
    public String editProductPage(Model model,@PathVariable Long id) {
        try {
            Product product = this.productRepository.findById(id).orElseThrow();
            List<Category> categories=this.categoryRepository.findAll();
            model.addAttribute("product", product);
            model.addAttribute("categories",categories);
            return "add-product";
        } catch (RuntimeException ex) {
            return "redirect:/products?error= Ne postoi takov produkt.";

        }
    }
    @PostMapping("/{id}/delete")
    @Secured("ROLE_ADMIN")
    public String deleteProduct(@PathVariable Long id){
        try {
            this.productService.deleteById(id);
        }
        //dokolku produktot e vo shopping cart da frla exception bidejki ne moze da go izbrise
        catch (ProductIsAlreadyInShoppingCartException ex){
            return String.format("redirect:/products?error=%s",ex.getMessage());
        }
        return "redirect:/products";
    }

}
