package com.sdtechno.sdcart.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sdtechno.sdcart.dto.ProductControllerCreateProductDto;
import com.sdtechno.sdcart.dto.ProductControllerUpdateProductDto;
import com.sdtechno.sdcart.dto.SearchCriteria;
import com.sdtechno.sdcart.exceptions.ResourceNotFoundException;
import com.sdtechno.sdcart.models.Product;
import com.sdtechno.sdcart.repositories.ProductRepository;
import com.sdtechno.sdcart.specifications.ProductSpecification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Product saveProductWithImage(Product product, MultipartFile imageFile) throws Exception {
        if (imageFile != null && !imageFile.isEmpty()) {
            Map<?, ?> uploadResult =
                    cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
            product.setImageUrl(uploadResult.get("secure_url").toString());
        }
        return productRepository.save(product);
    }


    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryIgnoreCase(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrandIgnoreCase(brand);
    }

    @Override
    public Page<Product> search(SearchCriteria criteria, Pageable pageable) {
        return productRepository.findAll(
                ProductSpecification.byCriteria(criteria),
                pageable
        );
    }

	@Override
	public ResponseEntity<Map<String, String>> saveProduct(ProductControllerCreateProductDto newProduct) throws IOException {
		Product product = new Product();
		product.setName(newProduct.getName());
		product.setPrice(newProduct.getPrice());
		product.setDescription(newProduct.getDescription());
		product.setRatings(newProduct.getRatings());
		product.setSeller(newProduct.getSeller());
		product.setStock(newProduct.getStock());
		product.setNumOfReviews(newProduct.getNoOfReviews());
		product.setCategory(newProduct.getCategory());
		product.setBrand(newProduct.getBrand());
		
		Map<String, Object> uploadResult = cloudinary.uploader().upload(
                newProduct.getImageFile().getBytes(),
                ObjectUtils.asMap("folder", "products")
        );
        product.setImageUrl(uploadResult.get("secure_url").toString());
        
        productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("Message","Product created"));
	}

	@Override
	public ResponseEntity<Map<String, String>> updateProduct(ProductControllerUpdateProductDto updateProduct,Long id) throws IOException {
		
		Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if (updateProduct.getName() != null) existingProduct.setName(updateProduct.getName());
        if (updateProduct.getDescription() != null) existingProduct.setDescription(updateProduct.getDescription());
        if (updateProduct.getPrice() != null && updateProduct.getPrice()> 0) existingProduct.setPrice(updateProduct.getPrice());
        if (updateProduct.getRatings() != null && updateProduct.getRatings()>= 0) existingProduct.setRatings(updateProduct.getRatings());
        if (updateProduct.getSeller()!= null) existingProduct.setSeller(updateProduct.getSeller());
        if (updateProduct.getStock() != null && updateProduct.getStock() >= 0) existingProduct.setStock(updateProduct.getStock());
        if (updateProduct.getNoOfReviews() != null && updateProduct.getNoOfReviews()>= 0) existingProduct.setNumOfReviews(updateProduct.getNoOfReviews());
        if (updateProduct.getCategory()!= null) existingProduct.setCategory(updateProduct.getCategory());
        if (updateProduct.getBrand() != null) existingProduct.setBrand(updateProduct.getBrand());

        // Handle new image upload
        if (updateProduct.getImageFile() != null && !updateProduct.getImageFile().isEmpty()) {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    updateProduct.getImageFile().getBytes(),
                    ObjectUtils.asMap("folder", "products")
            );
            existingProduct.setImageUrl(uploadResult.get("secure_url").toString());
        }

        productRepository.save(existingProduct);
        return ResponseEntity.accepted().body(Map.of("Message","Product Updated Successfully"));

	}
}