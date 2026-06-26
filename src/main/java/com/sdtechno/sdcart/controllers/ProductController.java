package com.sdtechno.sdcart.controllers;

import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sdtechno.sdcart.dto.GetProductsByCategoryResponseDto;
import com.sdtechno.sdcart.dto.PaginationProductControllGetAllProductsResponseDto;
import com.sdtechno.sdcart.dto.PaginationProductControllerGetAllProductRequestDto;
import com.sdtechno.sdcart.dto.PaginationSearchProductsRequestDto;
import com.sdtechno.sdcart.dto.ProductControllerCreateProductDto;
import com.sdtechno.sdcart.dto.ProductControllerGetAllProductsResponseDto;
import com.sdtechno.sdcart.dto.ProductControllerUpdateProductDto;
import com.sdtechno.sdcart.dto.ResponseProductByIdDto;
import com.sdtechno.sdcart.dto.ResponseSearchProductDto;
import com.sdtechno.sdcart.services.ProductService;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    
    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> createProduct(@ModelAttribute ProductControllerCreateProductDto newProduct) throws IOException {
        	return productService.saveProduct(newProduct);
    }

    @PutMapping(value = "/update/{id}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> updateProductById(
            @PathVariable Long id,@ModelAttribute ProductControllerUpdateProductDto updateProduct) throws IOException {  
        	return productService.updateProduct(updateProduct, id);
    }

    @GetMapping("/light")
    public PaginationProductControllGetAllProductsResponseDto<ProductControllerGetAllProductsResponseDto> getLightProducts(@ModelAttribute PaginationProductControllerGetAllProductRequestDto request) {
        return productService.getAllProducts(request);
    }

    @GetMapping("/product/{id}")
    public ResponseProductByIdDto getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
    	return productService.deleteProduct(id);
    }

    @GetMapping("/search")
    public PaginationProductControllGetAllProductsResponseDto<ResponseSearchProductDto> searchProducts(
            @RequestParam String q,@ModelAttribute PaginationSearchProductsRequestDto request) {
    	return productService.search(q, request);
    }


    @GetMapping("/category/{category}")
    public PaginationProductControllGetAllProductsResponseDto<GetProductsByCategoryResponseDto> getProductsByCategory(@PathVariable String category,@ModelAttribute PaginationSearchProductsRequestDto request) {
        return productService.getProductsByCategory(category,request);
    }

    @GetMapping("/brand/{brand}")
    public PaginationProductControllGetAllProductsResponseDto<GetProductsByCategoryResponseDto> getProductsByBrand(@PathVariable String brand, @ModelAttribute PaginationSearchProductsRequestDto request) {
        return productService.getProductsByBrand(brand, request);
    }

    @GetMapping("/test")
    public String testEndpoint() {
        return "GET /products/test is working";
    }
}
