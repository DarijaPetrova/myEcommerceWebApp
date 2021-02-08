package com.example.proekt.service;

import com.example.proekt.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    //List<Product> findAll(); bidejki gi koristam osnovnite metodi koi se gotovi od Jpa direkno so repository ke gi pristapuvam

    Product saveProduct(Product product, MultipartFile image) throws IOException;

    //bidejki find by id moze da frli exception dokolku ne postoi produktso toj id
    Product findById(Long productId);

    //bidejki pri brisenje treba da frla exception dokolku produktot e vo shoppingcart
    void deleteById(Long id);

    //za search
    List<Product> listAll(String keyword);


}
