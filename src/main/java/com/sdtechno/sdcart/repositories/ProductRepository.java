package com.sdtechno.sdcart.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.sdtechno.sdcart.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // ✅ Basic queries
    List<Product> findByPriceGreaterThan(double price);
    List<Product> findByPriceLessThan(double price);
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // ✅ Category / Brand filters
    List<Product> findByCategoryIgnoreCase(String category);
    List<Product> findByBrandIgnoreCase(String brand);
    List<Product> findByCategoryIgnoreCaseAndBrandIgnoreCase(String category, String brand);

    // ✅ Keyword + Category / Brand filters
    List<Product> findByNameContainingIgnoreCaseAndCategoryIgnoreCase(String keyword, String category);
    List<Product> findByNameContainingIgnoreCaseAndBrandIgnoreCase(String keyword, String brand);
    List<Product> findByNameContainingIgnoreCaseAndCategoryIgnoreCaseAndBrandIgnoreCase(
            String name, String category, String brand);

    // ✅ Price + Category / Brand
    List<Product> findByCategoryIgnoreCaseAndPriceLessThan(String category, double price);
    List<Product> findByCategoryIgnoreCaseAndPriceGreaterThan(String category, double price);

    List<Product> findByBrandIgnoreCaseAndPriceLessThan(String brand, double price);
    List<Product> findByBrandIgnoreCaseAndPriceGreaterThan(String brand, double price);
}
