package com.example.proekt.repository;

import com.example.proekt.enumerations.CartStatus;
import com.example.proekt.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
    //query koe vraka dali postoi takov korisnik ili ne
    boolean existsByUserUsernameAndStatus(String userId, CartStatus status);
    //query koe ni vraka shopping cart dokolku veke ima kreirana za toj korisnik, ili dokolku nema ja kreira
    Optional<ShoppingCart> findByUserUsernameAndStatus(String userId, CartStatus status);
}
