package com.sdtechno.sdcart.repositories;

import com.sdtechno.sdcart.models.CartItem;
import com.sdtechno.sdcart.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    void deleteByUser(User user);
}
