package com.nike.nike.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nike.nike.model.Cart;
import com.nike.nike.model.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems WHERE c.user = :user")
    Optional<Cart> findByUserWithItems(User user);

}
