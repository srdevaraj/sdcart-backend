package com.sdtechno.sdcart.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sdtechno.sdcart.exceptions.ResourceNotFoundException;
import com.sdtechno.sdcart.models.Product;
import com.sdtechno.sdcart.services.ProductService;
import java.io.IOException;


@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productservice;

    @Autowired
    private Cloudinary cloudinary;

    // Create new product with image (Cloudinary)
    @PostMapping(consumes = {"multipart/form-data"})
    public Product createProduct(@ModelAttribute Product product,
                                 @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("secure_url");
            product.setImageUrl(imageUrl);
        }
        return productservice.saveProduct(product);
    }

    // Update product with optional image (Cloudinary)
    @PutMapping(value = "/product/{id}", consumes = {"multipart/form-data"})
    public Product updateProductById(@PathVariable Long id,
                                     @ModelAttribute Product product,
                                     @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        Product existingProduct = productservice.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if (product.getName() != null) existingProduct.setName(product.getName());
        if (product.getDescription() != null) existingProduct.setDescription(product.getDescription());
        if (product.getPrice() != 0) existingProduct.setPrice(product.getPrice());
        if (product.getRatings() != 0) existingProduct.setRatings(product.getRatings());
        if (product.getSeller() != null) existingProduct.setSeller(product.getSeller());
        if (product.getStock() != 0) existingProduct.setStock(product.getStock());
        if (product.getNumOfReviews() != 0) existingProduct.setNumOfReviews(product.getNumOfReviews());

        if (imageFile != null && !imageFile.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("secure_url");
            existingProduct.setImageUrl(imageUrl);
        }

        return productservice.saveProduct(existingProduct);
    }

    // ❌ REMOVE the old local upload method
    // private String saveImageToUploads(...) { ... }
}
