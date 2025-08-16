package com.sdtechno.sdcart.controllers;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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
    public ResponseEntity<Product> createProduct(
            @RequestParam("name") String name,
            @RequestParam("price") double price,
            @RequestParam("description") String description,
            @RequestParam("ratings") double ratings,
            @RequestParam("seller") String seller,
            @RequestParam("stock") int stock,
            @RequestParam("numOfReviews") int numOfReviews,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "brand", required = false) String brand,
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
            product.setCategory(category);
            product.setBrand(brand);

            // Handle image upload
            if (imageFile != null && !imageFile.isEmpty()) {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(
                        imageFile.getBytes(),
                        ObjectUtils.asMap("folder", "products")
                );
                product.setImageUrl(uploadResult.get("secure_url").toString());
            }

            Product savedProduct = productService.saveProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);

        } catch (IOException e) {
            logger.error("Error uploading image for product '{}': {}", name, e.getMessage(), e);
            throw new RuntimeException("Image upload failed: " + e.getMessage(), e);
        }
    }

    /**
     * ✅ Update product (Admin only) with optional image upload
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
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        try {
            Product existingProduct = productService.getProductById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

            if (name != null) existingProduct.setName(name);
            if (description != null) existingProduct.setDescription(description);
            if (price != null && price > 0) existingProduct.setPrice(price);
            if (ratings != null && ratings >= 0) existingProduct.setRatings(ratings);
            if (seller != null) existingProduct.setSeller(seller);
            if (stock != null && stock >= 0) existingProduct.setStock(stock);
            if (numOfReviews != null && numOfReviews >= 0) existingProduct.setNumOfReviews(numOfReviews);
            if (category != null) existingProduct.setCategory(category);
            if (brand != null) existingProduct.setBrand(brand);

            // Handle new image upload
            if (imageFile != null && !imageFile.isEmpty()) {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(
                        imageFile.getBytes(),
                        ObjectUtils.asMap("folder", "products")
                );
                existingProduct.setImageUrl(uploadResult.get("secure_url").toString());
            }

            Product updatedProduct = productService.saveProduct(existingProduct);
            return ResponseEntity.ok(updatedProduct);

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
     * sort example: sort=name,desc  (defaults to id,asc)
     */
    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort) {

        // Sorting (robust parsing)
        String[] sortParams = sort.split(",", 2);
        String sortBy = (sortParams.length > 0 && !sortParams[0].isBlank()) ? sortParams[0] : "id";
        Sort.Direction direction = (sortParams.length > 1 && "desc".equalsIgnoreCase(sortParams[1]))
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        // Get all products (you can later replace with a repository-level search)
        List<Product> products = productService.getAllProducts();

        // Apply filters
        List<Product> filtered = products.stream()
                .filter(p -> query == null
                        || (p.getName() != null && p.getName().toLowerCase().contains(query.toLowerCase()))
                        || (p.getDescription() != null && p.getDescription().toLowerCase().contains(query.toLowerCase())))
                .filter(p -> category == null
                        || (p.getCategory() != null && p.getCategory().equalsIgnoreCase(category)))
                .filter(p -> brand == null
                        || (p.getBrand() != null && p.getBrand().equalsIgnoreCase(brand)))
                .filter(p -> minPrice == null || p.getPrice() >= minPrice)
                .filter(p -> maxPrice == null || p.getPrice() <= maxPrice)
                .collect(Collectors.toList());

        // Convert list to page (safe for out-of-range pages)
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtered.size());
        List<Product> content = (start >= filtered.size()) ? Collections.emptyList() : filtered.subList(start, end);

        Page<Product> productPage = new PageImpl<>(content, pageable, filtered.size());
        return ResponseEntity.ok(productPage);
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
