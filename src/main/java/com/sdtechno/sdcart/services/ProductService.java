package com.sdtechno.sdcart.services;

import com.sdtechno.sdcart.models.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    List<Product> getProductsGreaterThanPrice(double price);
    List<Product> getProductsLessThanPrice(double price);
    Product saveProduct(Product product);
    Product saveProductWithImage(Product product, MultipartFile imageFile) throws Exception;
    List<Product> searchProducts(String keyword);
    boolean deleteProduct(Long id);

    // ✅ Update product
    Product updateProduct(Long id, Product updatedProduct, MultipartFile imageFile) throws Exception;

    // ✅ Category & Brand
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);

    // ✅ Combined search with optional filters
    List<Product> searchProducts(String query, String category, String brand);
}
