package com.sdtechno.sdcart.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sdtechno.sdcart.dto.SearchCriteria;
import com.sdtechno.sdcart.models.Product;
import com.sdtechno.sdcart.repositories.ProductRepository;
import com.sdtechno.sdcart.specifications.ProductSpecification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Cloudinary cloudinary;

    // =========================
    // BASIC CRUD
    // =========================

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // =========================
    // IMAGE UPLOAD
    // =========================

    @Override
    public Product saveProductWithImage(Product product, MultipartFile imageFile) throws Exception {
        if (imageFile != null && !imageFile.isEmpty()) {
            Map<?, ?> uploadResult =
                    cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
            product.setImageUrl(uploadResult.get("secure_url").toString());
        }
        return productRepository.save(product);
    }

    // =========================
    // UPDATE PRODUCT
    // =========================

    @Override
    public Product updateProduct(Long id, Product updatedProduct, MultipartFile imageFile) throws Exception {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        if (updatedProduct.getName() != null)
            existingProduct.setName(updatedProduct.getName());

        if (updatedProduct.getDescription() != null)
            existingProduct.setDescription(updatedProduct.getDescription());

        if (updatedProduct.getPrice() > 0)
            existingProduct.setPrice(updatedProduct.getPrice());

        if (updatedProduct.getRatings() >= 0)
            existingProduct.setRatings(updatedProduct.getRatings());

        if (updatedProduct.getSeller() != null)
            existingProduct.setSeller(updatedProduct.getSeller());

        if (updatedProduct.getStock() >= 0)
            existingProduct.setStock(updatedProduct.getStock());

        if (updatedProduct.getNumOfReviews() >= 0)
            existingProduct.setNumOfReviews(updatedProduct.getNumOfReviews());

        if (updatedProduct.getCategory() != null)
            existingProduct.setCategory(updatedProduct.getCategory());

        if (updatedProduct.getBrand() != null)
            existingProduct.setBrand(updatedProduct.getBrand());

        if (imageFile != null && !imageFile.isEmpty()) {
            Map<?, ?> uploadResult =
                    cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
            existingProduct.setImageUrl(uploadResult.get("secure_url").toString());
        }

        return productRepository.save(existingProduct);
    }

    // =========================
    // CATEGORY & BRAND FILTERS
    // =========================

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryIgnoreCase(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrandIgnoreCase(brand);
    }

    // =========================
    // 🔥 SMART SEARCH (AMAZON STYLE)
    // =========================

    @Override
    public Page<Product> search(SearchCriteria criteria, Pageable pageable) {
        return productRepository.findAll(
                ProductSpecification.byCriteria(criteria),
                pageable
        );
    }
}
