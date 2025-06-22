package com.sdtechno.sdcart.controllers;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sdtechno.sdcart.exceptions.ResourceNotFoundException;
import com.sdtechno.sdcart.models.Product;
import com.sdtechno.sdcart.services.ProductService;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")  // Adjust CORS as needed
public class ProductController {

    @Autowired
    private ProductService productservice;

    @GetMapping
    public List<Product> getAllProducts() {
        return productservice.getAllProducts();
    }

    @GetMapping("/product/greaterThan/{price}")
    public List<Product> getProductsGreaterThanPrice(@PathVariable double price) {
        return productservice.getProductsGreaterThanPrice(price);
    }

    @GetMapping("/product/lessThan/{price}")
    public List<Product> getProductsLessThanPrice(@PathVariable double price) {
        return productservice.getProductsLessThanPrice(price);
    }

    @GetMapping("/product/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productservice.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    // Change here: use @ModelAttribute instead of @RequestPart("product")
    @PostMapping(consumes = {"multipart/form-data"})
    public Product createProduct(@ModelAttribute Product product,
                                 @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) throws Exception {
        return productservice.saveProductWithImage(product, imageFile);
    }

    // Same update for PUT
    @PutMapping(value = "/product/{id}", consumes = {"multipart/form-data"})
    public Product updateProductById(@PathVariable Long id,
                                     @ModelAttribute Product product,
                                     @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) throws Exception {
        Product existingProduct = productservice.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // Update only if fields are present
        if (product.getName() != null) existingProduct.setName(product.getName());
        if (product.getDescription() != null) existingProduct.setDescription(product.getDescription());
        if (product.getPrice() != 0) existingProduct.setPrice(product.getPrice());
        if (product.getRatings() != 0) existingProduct.setRatings(product.getRatings());
        if (product.getSeller() != null) existingProduct.setSeller(product.getSeller());
        if (product.getStock() != 0) existingProduct.setStock(product.getStock());
        if (product.getNumOfReviews() != 0) existingProduct.setNumOfReviews(product.getNumOfReviews());

        if (imageFile != null && !imageFile.isEmpty()) {
            existingProduct.setImage(imageFile.getBytes());
        }

        return productservice.saveProduct(existingProduct);
    }

    @GetMapping("/product/{id}/image")
    public ResponseEntity<String> getProductImage(@PathVariable Long id) {
        Product product = productservice.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if (product.getImage() == null) {
            return ResponseEntity.noContent().build();
        }

        String base64Image = Base64.getEncoder().encodeToString(product.getImage());
        return ResponseEntity.ok(base64Image);
    }

    @DeleteMapping("/product/deleteById/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable Long id) {
        productservice.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productservice.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
