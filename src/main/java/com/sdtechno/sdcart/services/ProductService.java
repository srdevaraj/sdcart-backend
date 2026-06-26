package com.sdtechno.sdcart.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.sdtechno.sdcart.dto.GetProductsByCategoryResponseDto;
import com.sdtechno.sdcart.dto.PaginationProductControllGetAllProductsResponseDto;
import com.sdtechno.sdcart.dto.PaginationProductControllerGetAllProductRequestDto;
import com.sdtechno.sdcart.dto.PaginationSearchProductsRequestDto;
import com.sdtechno.sdcart.dto.ProductControllerCreateProductDto;
import com.sdtechno.sdcart.dto.ProductControllerGetAllProductsResponseDto;
import com.sdtechno.sdcart.dto.ProductControllerUpdateProductDto;
import com.sdtechno.sdcart.dto.ResponseProductByIdDto;
import com.sdtechno.sdcart.dto.ResponseSearchProductDto;
import com.sdtechno.sdcart.dto.SearchCriteria;
import com.sdtechno.sdcart.models.Product;

public interface ProductService {

    ResponseProductByIdDto getProductById(Long id);
    ResponseEntity<Map<String, String>> deleteProduct(Long id);
    Product saveProductWithImage(Product product, MultipartFile imageFile) throws Exception;
    ResponseEntity<Map<String, String>> updateProduct(ProductControllerUpdateProductDto updateProduct, Long id)
			throws IOException;
	ResponseEntity<Map<String, String>> saveProduct(ProductControllerCreateProductDto newProduct) throws IOException;
	PaginationProductControllGetAllProductsResponseDto<ProductControllerGetAllProductsResponseDto> getAllProducts(PaginationProductControllerGetAllProductRequestDto request);
	PaginationProductControllGetAllProductsResponseDto<ResponseSearchProductDto> search(String q,PaginationSearchProductsRequestDto request);
	PaginationProductControllGetAllProductsResponseDto<GetProductsByCategoryResponseDto> getProductsByCategory(
			String category, PaginationSearchProductsRequestDto request);
	PaginationProductControllGetAllProductsResponseDto<GetProductsByCategoryResponseDto> getProductsByBrand(
			String brand, PaginationSearchProductsRequestDto request);

}

