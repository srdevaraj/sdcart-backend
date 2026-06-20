package com.sdtechno.sdcart.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.sdtechno.sdcart.dto.ProductControllerCreateProductDto;
import com.sdtechno.sdcart.dto.ProductControllerUpdateProductDto;
import com.sdtechno.sdcart.dto.SearchCriteria;
import com.sdtechno.sdcart.models.Product;

public interface ProductService {

    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    boolean deleteProduct(Long id);
    Product saveProductWithImage(Product product, MultipartFile imageFile) throws Exception;
    ResponseEntity<Map<String, String>> updateProduct(ProductControllerUpdateProductDto updateProduct, Long id)
			throws IOException;
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    Page<Product> search(SearchCriteria criteria, Pageable pageable);
	ResponseEntity<Map<String, String>> saveProduct(ProductControllerCreateProductDto newProduct) throws IOException;
	
}

