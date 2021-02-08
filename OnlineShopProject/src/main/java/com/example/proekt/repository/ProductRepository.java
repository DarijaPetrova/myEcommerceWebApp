package com.example.proekt.repository;

import com.example.proekt.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findAllByCategoryId(Long id);

    //za search
    @Query("SELECT p FROM Product p WHERE CONCAT(p.name, ' ', p.category, ' ', p.quantity, ' ', p.price) LIKE %?1%")
    List<Product> search(String keyword);

}
