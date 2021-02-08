package com.example.proekt.repository;

import com.example.proekt.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    CartItem findByProductIdAndShoppingCartId(Long productId, Long shopingCartId);

   List<CartItem> findByProductId(Long productId);

}
