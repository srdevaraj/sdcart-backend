package com.sdtechno.sdcart.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.sdtechno.sdcart.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // ✅ Existing queries
    List<Product> findByPriceGreaterThan(double price);
    List<Product> findByPriceLessThan(double price);
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // ✅ New queries for filtering
    List<Product> findByCategoryIgnoreCase(String category);
    List<Product> findByBrandIgnoreCase(String brand);
    List<Product> findByCategoryAndBrandAllIgnoreCase(String category, String brand);

    // ✅ Combination queries
    List<Product> findByCategoryAndPriceLessThan(String category, double price);
    List<Product> findByCategoryAndPriceGreaterThan(String category, double price);

    List<Product> findByBrandAndPriceLessThan(String brand, double price);
    List<Product> findByBrandAndPriceGreaterThan(String brand, double price);
}
