package com.sdtechno.sdcart.services;

import com.sdtechno.sdcart.dto.SearchCriteria;
import com.sdtechno.sdcart.models.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    // Basic CRUD
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    Product saveProduct(Product product);
    boolean deleteProduct(Long id);

    // Image upload
    Product saveProductWithImage(Product product, MultipartFile imageFile) throws Exception;

    // Update
    Product updateProduct(Long id, Product updatedProduct, MultipartFile imageFile) throws Exception;

    // Filters
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);

    // 🔥 SMART SEARCH (Amazon-style)
    Page<Product> search(SearchCriteria criteria, Pageable pageable);
}
