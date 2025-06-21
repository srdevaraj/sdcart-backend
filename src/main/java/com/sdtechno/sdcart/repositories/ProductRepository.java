package com.sdtechno.sdcart.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.sdtechno.sdcart.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByPriceGreaterThan(double price);
    List<Product> findByPriceLessThan(double price);

}