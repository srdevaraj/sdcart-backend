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
     * ✅ Create product (Admin only) with image upload
     */
    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> createProduct(
            @RequestParam("name") String name,
            @RequestParam("price") double price,
            @RequestParam("description") String description,
            @RequestParam("ratings") double ratings,
            @RequestParam("seller") String seller,
            @RequestParam("stock") int stock,
            @RequestParam("numOfReviews") int numOfReviews,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        try {
            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setDescription(description);
            product.setRatings(ratings);
            product.setSeller(seller);
            product.setStock(stock);
            product.setNumOfReviews(numOfReviews);

            // Handle image upload
            if (imageFile != null && !imageFile.isEmpty()) {
                @SuppressWarnings("unchecked")
				Map<String, Object> uploadResult = cloudinary.uploader().upload(
                        imageFile.getBytes(),
                        ObjectUtils.asMap("folder", "products")
                );
                String imageUrl = (String) uploadResult.get("secure_url");
                product.setImageUrl(imageUrl);
            }

            Product savedProduct = productService.saveProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ✅ Update product (Admin only) with image upload
     */
    @PutMapping(value = "/update/{id}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> updateProductById(
            @PathVariable Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "ratings", required = false) Double ratings,
            @RequestParam(value = "seller", required = false) String seller,
            @RequestParam(value = "stock", required = false) Integer stock,
            @RequestParam(value = "numOfReviews", required = false) Integer numOfReviews,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        try {
            Product existingProduct = productService.getProductById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

            // Update only if provided
            if (name != null) existingProduct.setName(name);
            if (description != null) existingProduct.setDescription(description);
            if (price != null && price > 0) existingProduct.setPrice(price);
            if (ratings != null && ratings > 0) existingProduct.setRatings(ratings);
            if (seller != null) existingProduct.setSeller(seller);
            if (stock != null && stock > 0) existingProduct.setStock(stock);
            if (numOfReviews != null && numOfReviews > 0) existingProduct.setNumOfReviews(numOfReviews);

            // Handle image upload
            if (imageFile != null && !imageFile.isEmpty()) {
                @SuppressWarnings("unchecked")
				Map<String, Object> uploadResult = cloudinary.uploader().upload(
                        imageFile.getBytes(),
                        ObjectUtils.asMap("folder", "products")
                );
                String imageUrl = (String) uploadResult.get("secure_url");
                existingProduct.setImageUrl(imageUrl);
            }

            Product updatedProduct = productService.saveProduct(existingProduct);
            return ResponseEntity.ok(updatedProduct);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
