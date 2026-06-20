package com.sdtechno.sdcart.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloudinary.Cloudinary;
import com.sdtechno.sdcart.dto.ProductControllerCreateProductDto;
import com.sdtechno.sdcart.dto.ProductControllerUpdateProductDto;
import com.sdtechno.sdcart.dto.SearchCriteria;
import com.sdtechno.sdcart.exceptions.ResourceNotFoundException;
import com.sdtechno.sdcart.models.Product;
import com.sdtechno.sdcart.search.SearchQueryParser;
import com.sdtechno.sdcart.services.ProductService;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private Cloudinary cloudinary;

    /**
     * ✅ Create product (Admin only) with image upload
     */
    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> createProduct(@RequestParam ProductControllerCreateProductDto newProduct) {

        try {
        	return productService.saveProduct(newProduct);
        } catch (IOException e) {
            logger.error("Error uploading image for product '{}': {}", newProduct.getName(), e.getMessage(), e);
            throw new RuntimeException("Image upload failed: " + e.getMessage(), e);
        }
    }

    /**
     * ✅ Update product (Admin only) with optional image upload
     */
    @PutMapping(value = "/update/{id}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> updateProductById(
            @PathVariable Long id,@RequestParam ProductControllerUpdateProductDto updateProduct) {

        try {
        	return productService.updateProduct(updateProduct, id);
        } catch (IOException e) {
            logger.error("Error updating product with id '{}': {}", id, e.getMessage(), e);
            throw new RuntimeException("Image upload failed during update: " + e.getMessage(), e);
        }
    }

    /**
     * ✅ Get light-weight product list
     */
    @GetMapping("/light")
    public List<Map<String, Object>> getLightProducts() {
        return productService.getAllProducts().stream()
                .map(product -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", product.getId());
                    map.put("name", product.getName());
                    map.put("price", product.getPrice());
                    map.put("imageUrl", product.getImageUrl());
                    return map;
                })
                .collect(Collectors.toList());
    }

    /**
     * ✅ Get full product details by ID
     */
    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    /**
     * ✅ Delete product (Admin only)
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.noContent().build();
        }
        throw new ResourceNotFoundException("Product not found with id: " + id);
    }

    /**
     * ✅ Search + Filter + Pagination + Sorting
     */
    @Autowired
    private SearchQueryParser searchQueryParser;

    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        SearchCriteria criteria = searchQueryParser.parse(q);

        logger.info("Search Query: {}", q);
        logger.info("Parsed Criteria: {}", criteria);

        Page<Product> result = productService.search(criteria, pageable	);
        return ResponseEntity.ok(result);
    }


    /**
     * ✅ Direct filter by category
     */
    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }

    /**
     * ✅ Direct filter by brand
     */
    @GetMapping("/brand/{brand}")
    public List<Product> getProductsByBrand(@PathVariable String brand) {
        return productService.getProductsByBrand(brand);
    }

    /**
     * ✅ Test endpoint
     */
    @GetMapping("/test")
    public String testEndpoint() {
        return "GET /products/test is working";
    }
}
