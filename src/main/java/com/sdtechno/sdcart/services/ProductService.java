package com.sdtechno.sdcart.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sdtechno.sdcart.models.Product;
import com.sdtechno.sdcart.repositories.ProductRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productrepository;

    public List<Product> getAllProducts() {
        return productrepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productrepository.findById(id);
    }
    public List<Product> getProductsGreaterThanPrice(double price) {
        return productrepository.findByPriceGreaterThan(price);
    }
    public List<Product> getProductsLessThanPrice(double price) {
        return productrepository.findByPriceLessThan(price);
    }
    public Product saveProduct(Product product) {
        return productrepository.save(product);
    }

    public Product saveProductWithImage(Product product, MultipartFile imageFile) throws Exception {
        if (imageFile != null && !imageFile.isEmpty()) {
            product.setImage(imageFile.getBytes());
        }
        return productrepository.save(product);
    }
    public List<Product> searchProducts(String keyword) {
        return productrepository.findByNameContainingIgnoreCase(keyword);
    }

    public void deleteById(Long id) {
        productrepository.deleteById(id);
    }

}
