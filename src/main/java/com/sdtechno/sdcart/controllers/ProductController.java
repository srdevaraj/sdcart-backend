package com.sdtechno.sdcart.controllers;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sdtechno.sdcart.exceptions.ResourceNotFoundException;
import com.sdtechno.sdcart.models.Product;
import com.sdtechno.sdcart.services.ProductService;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private Cloudinary cloudinary;

    /**
     * ✅ Create product (Admin only)
     */
    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public Product createProduct(@ModelAttribute Product product,
                                 @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    imageFile.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("secure_url");
            product.setImageUrl(imageUrl);
        }
        return productService.saveProduct(product);
    }

    /**
     * ✅ Update product (Admin only)
     */
    @PutMapping(value = "/update/{id}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public Product updateProductById(@PathVariable Long id,
                                     @ModelAttribute Product product,
                                     @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {

        Product existingProduct = productService.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // Update only non-null fields
        if (product.getName() != null) existingProduct.setName(product.getName());
        if (product.getDescription() != null) existingProduct.setDescription(product.getDescription());
        if (product.getPrice() > 0) existingProduct.setPrice(product.getPrice());
        if (product.getRatings() > 0) existingProduct.setRatings(product.getRatings());
        if (product.getSeller() != null) existingProduct.setSeller(product.getSeller());
        if (product.getStock() > 0) existingProduct.setStock(product.getStock());
        if (product.getNumOfReviews() > 0) existingProduct.setNumOfReviews(product.getNumOfReviews());

        if (imageFile != null && !imageFile.isEmpty()) {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    imageFile.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("secure_url");
            existingProduct.setImageUrl(imageUrl);
        }

        return productService.saveProduct(existingProduct);
    }

    /**
     * ✅ Get all products (light data)
     */
    @GetMapping("/light")
    public List<Map<String, Object>> getLightProducts() {
        return productService.getAllProducts().stream()
                .map(product -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", product.getId());
                    map.put("name", product.getName());
                    map.put("price", product.getPrice());
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
     * ✅ Test endpoint
     */
    @GetMapping("/test")
    public String testEndpoint() {
        return "GET /products/test is working";
    }
}
