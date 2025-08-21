package com.sdtechno.sdcart.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sdtechno.sdcart.models.Product;
import com.sdtechno.sdcart.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getProductsGreaterThanPrice(double price) {
        return productRepository.findByPriceGreaterThan(price);
    }

    @Override
    public List<Product> getProductsLessThanPrice(double price) {
        return productRepository.findByPriceLessThan(price);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product saveProductWithImage(Product product, MultipartFile imageFile) throws Exception {
        if (imageFile != null && !imageFile.isEmpty()) {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("secure_url");
            product.setImageUrl(imageUrl);
        }
        return productRepository.save(product);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public boolean deleteProduct(Long id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Product updateProduct(Long id, Product updatedProduct, MultipartFile imageFile) throws Exception {
        Optional<Product> existingProductOpt = productRepository.findById(id);

        if (existingProductOpt.isEmpty()) {
            throw new RuntimeException("Product with ID " + id + " not found.");
        }

        Product existingProduct = existingProductOpt.get();

        if (updatedProduct.getName() != null) existingProduct.setName(updatedProduct.getName());
        if (updatedProduct.getDescription() != null) existingProduct.setDescription(updatedProduct.getDescription());
        if (updatedProduct.getPrice() > 0) existingProduct.setPrice(updatedProduct.getPrice());
        if (updatedProduct.getRatings() >= 0) existingProduct.setRatings(updatedProduct.getRatings());
        if (updatedProduct.getSeller() != null) existingProduct.setSeller(updatedProduct.getSeller());
        if (updatedProduct.getStock() >= 0) existingProduct.setStock(updatedProduct.getStock());
        if (updatedProduct.getNumOfReviews() >= 0) existingProduct.setNumOfReviews(updatedProduct.getNumOfReviews());
        if (updatedProduct.getCategory() != null) existingProduct.setCategory(updatedProduct.getCategory());
        if (updatedProduct.getBrand() != null) existingProduct.setBrand(updatedProduct.getBrand());

        if (imageFile != null && !imageFile.isEmpty()) {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("secure_url");
            existingProduct.setImageUrl(imageUrl);
        }

        return productRepository.save(existingProduct);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryIgnoreCase(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrandIgnoreCase(brand);
    }

    // ✅ Combined search with null/empty safety
    @Override
    public List<Product> searchProducts(String query, String category, String brand) {
        // normalize empty strings to null
        if (query != null && query.trim().isEmpty()) query = null;
        if (category != null && category.trim().isEmpty()) category = null;
        if (brand != null && brand.trim().isEmpty()) brand = null;

        if (query != null && category != null && brand != null) {
            return productRepository.findByNameContainingIgnoreCaseAndCategoryIgnoreCaseAndBrandIgnoreCase(query, category, brand);
        } else if (query != null && category != null) {
            return productRepository.findByNameContainingIgnoreCaseAndCategoryIgnoreCase(query, category);
        } else if (query != null && brand != null) {
            return productRepository.findByNameContainingIgnoreCaseAndBrandIgnoreCase(query, brand);
        } else if (category != null && brand != null) {
            return productRepository.findByCategoryIgnoreCaseAndBrandIgnoreCase(category, brand);
        } else if (query != null) {
            return productRepository.findByNameContainingIgnoreCase(query);
        } else if (category != null) {
            return productRepository.findByCategoryIgnoreCase(category);
        } else if (brand != null) {
            return productRepository.findByBrandIgnoreCase(brand);
        } else {
            return productRepository.findAll();
        }
    }
}
