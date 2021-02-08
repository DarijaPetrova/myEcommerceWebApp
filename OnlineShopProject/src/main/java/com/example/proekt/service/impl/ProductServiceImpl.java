package com.example.proekt.service.impl;

import com.example.proekt.exceptions.ProductIsAlreadyInShoppingCartException;
import com.example.proekt.exceptions.ProductNotFoundException;
import com.example.proekt.exceptions.UserNotFoundException;
import com.example.proekt.model.CartItem;
import com.example.proekt.model.Category;
import com.example.proekt.model.Product;
import com.example.proekt.repository.CartItemRepository;
import com.example.proekt.repository.CategoryRepository;
import com.example.proekt.repository.ProductRepository;
import com.example.proekt.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public Product saveProduct(Product product, MultipartFile image) throws IOException {
        //za zacuvuvanje na kategorijata
        Category category = this.categoryRepository.findById(product.getCategory().getId()).orElseThrow();
        product.setCategory(category);
        if (image != null && !image.getName().isEmpty()) {
            byte[] bytes = image.getBytes(); // gi zema bajtite od slikata
            String base64Image = String.format("data:%s;base64,%s", image.getContentType(), Base64.getEncoder().encodeToString(bytes));
            //book.setImage(image);
            product.setImageBase64(base64Image);
        }
        return this.productRepository.save(product);
    }

    @Override
    public Product findById(Long productId) {
        return this.productRepository.findById(productId)
                .orElseThrow(()->new ProductNotFoundException(productId));
    }

    @Override
    public void deleteById(Long id) {
        Product product=this.productRepository.findById(id).orElseThrow();


        List<CartItem> cartItems=this.cartItemRepository.findByProductId(product.getId());


        if(cartItems.size()>0){
            throw new ProductIsAlreadyInShoppingCartException(product.getName());
        }
        this.productRepository.deleteById(id);
    }
    //za search
    @Override
    public List<Product> listAll(String keyword) {
        if (keyword != null) {
            return productRepository.search(keyword);
        }
        return productRepository.findAll();
    }



}
