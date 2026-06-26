package com.sdtechno.sdcart.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sdtechno.sdcart.controllers.ProductController;
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
import com.sdtechno.sdcart.exceptions.ResourceNotFoundException;
import com.sdtechno.sdcart.models.Product;
import com.sdtechno.sdcart.repositories.ProductRepository;
import com.sdtechno.sdcart.search.SearchQueryParser;
import com.sdtechno.sdcart.specifications.ProductSpecification;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private SearchQueryParser searchQueryParser;

    @Override
    public PaginationProductControllGetAllProductsResponseDto<ProductControllerGetAllProductsResponseDto> getAllProducts(PaginationProductControllerGetAllProductRequestDto request){
    	Sort sort = request.getDirection().equalsIgnoreCase("asc") ? Sort.by(request.getSortBy()).ascending() : 
    		Sort.by(request.getSortBy()).descending();
    	Pageable pageable = PageRequest.of(request.getPage(),request.getSize(),sort);
    	Page<Product> productPages = productRepository.findAll(pageable);
    	Page<ProductControllerGetAllProductsResponseDto> responsePage = productPages.map(productPage->
    			ProductControllerGetAllProductsResponseDto.builder()
    				.id(productPage.getId())
    				.name(productPage.getName())
    				.price(productPage.getPrice())
    				.imageUrl(productPage.getImageUrl())
    				.build()
    		);
    	return PaginationProductControllGetAllProductsResponseDto.<ProductControllerGetAllProductsResponseDto>builder()
    			.content(responsePage.getContent())
    			.page(responsePage.getNumber())
    			.size(responsePage.getSize())
    			.totalPages(responsePage.getTotalPages())
    			.totalElements(responsePage.getNumberOfElements())
    			.last(responsePage.isLast())
    			.build();
    }
    	
    @Override
    public ResponseProductByIdDto getProductById(Long id) {
    	Product product = productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product not found..."));
    	return ResponseProductByIdDto.builder()
    			.name(product.getName())
    			.price(product.getPrice())
    			.description(product.getDescription())
    			.ratings(product.getRatings())
    			.seller(product.getSeller())
    			.stock(product.getStock())
    			.numOfReviews(product.getNumOfReviews())
    			.imageUrl(product.getImageUrl())
    			.category(product.getCategory())
    			.brand(product.getBrand())
    			.build();
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteProduct(Long id) {
    	Product product = productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product Not fouund"));
    	productRepository.delete(product);
    	return ResponseEntity.ok().body(Map.of("Message","Product Deleted Successfully"));
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
    public PaginationProductControllGetAllProductsResponseDto<GetProductsByCategoryResponseDto>
            getProductsByCategory(String category,
                                  PaginationSearchProductsRequestDto request) {

        Sort sort = request.getDirection().equalsIgnoreCase("asc")
                ? Sort.by(request.getSortBy()).ascending()
                : Sort.by(request.getSortBy()).descending();

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                sort
        );

        Page<Product> productPages =
                productRepository.findByCategoryIgnoreCase(category, pageable);

        Page<GetProductsByCategoryResponseDto> responsePage =
                productPages.map(product ->
                        GetProductsByCategoryResponseDto.builder()
                                .name(product.getName())
                                .price(product.getPrice())
                                .description(product.getDescription())
                                .ratings(product.getRatings())
                                .seller(product.getSeller())
                                .stock(product.getStock())
                                .numOfReviews(product.getNumOfReviews())
                                .imageUrl(product.getImageUrl())
                                .category(product.getCategory())
                                .brand(product.getBrand())
                                .build()
                );

        return PaginationProductControllGetAllProductsResponseDto
                .<GetProductsByCategoryResponseDto>builder()
                .content(responsePage.getContent())
                .page(responsePage.getNumber())
                .size(responsePage.getSize())
                .totalPages(responsePage.getTotalPages())
                .totalElements((int)responsePage.getTotalElements())
                .last(responsePage.isLast())
                .build();
    }
    
    @Override
    public PaginationProductControllGetAllProductsResponseDto<GetProductsByCategoryResponseDto>
            getProductsByBrand(String brand,
                                  PaginationSearchProductsRequestDto request) {

        Sort sort = request.getDirection().equalsIgnoreCase("asc")
                ? Sort.by(request.getSortBy()).ascending()
                : Sort.by(request.getSortBy()).descending();

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                sort
        );

        Page<Product> productPages =
                productRepository.findByBrandIgnoreCase(brand, pageable);

        Page<GetProductsByCategoryResponseDto> responsePage =
                productPages.map(product ->
                        GetProductsByCategoryResponseDto.builder()
                                .name(product.getName())
                                .price(product.getPrice())
                                .description(product.getDescription())
                                .ratings(product.getRatings())
                                .seller(product.getSeller())
                                .stock(product.getStock())
                                .numOfReviews(product.getNumOfReviews())
                                .imageUrl(product.getImageUrl())
                                .category(product.getCategory())
                                .brand(product.getBrand())
                                .build()
                );

        return PaginationProductControllGetAllProductsResponseDto
                .<GetProductsByCategoryResponseDto>builder()
                .content(responsePage.getContent())
                .page(responsePage.getNumber())
                .size(responsePage.getSize())
                .totalPages(responsePage.getTotalPages())
                .totalElements((int)responsePage.getTotalElements())
                .last(responsePage.isLast())
                .build();
    }

	@Override
	public ResponseEntity<Map<String, String>> saveProduct(ProductControllerCreateProductDto newProduct) {
		try {
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
			
			Map<String, Object> uploadResult;
				uploadResult = cloudinary.uploader().upload(
				        newProduct.getImageFile().getBytes(),
				        ObjectUtils.asMap("folder", "products")
				);
			
	        product.setImageUrl(uploadResult.get("secure_url").toString());
	        
	        productRepository.save(product);
	        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("Message","Product created"));
		} catch (IOException e) {
			logger.error("Error uploading image for product '{}': {}", newProduct.getName(), e.getMessage(), e);
            throw new RuntimeException("Image upload failed: " + e.getMessage(), e);
        
		}
	}

	@Override
	public ResponseEntity<Map<String, String>> updateProduct(ProductControllerUpdateProductDto updateProduct,Long id){
		
		try {
		
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
		} catch (IOException e) {
            logger.error("Error updating product with id '{}': {}", id, e.getMessage(), e);
            throw new RuntimeException("Image upload failed during update: " + e.getMessage(), e);
        }

	}

	@Override
	public PaginationProductControllGetAllProductsResponseDto<ResponseSearchProductDto> search(String q,PaginationSearchProductsRequestDto request) {
	
		Sort sort = request.getDirection().equalsIgnoreCase("asc") ? Sort.by(request.getSortBy()).ascending() : 
			Sort.by(request.getSortBy()).descending();
		
		Pageable pageable = PageRequest.of(request.getPage(),request.getSize(),sort);
        SearchCriteria criteria = searchQueryParser.parse(q);
		Page<Product> productPages = productRepository.findAll(ProductSpecification.byCriteria(criteria),pageable);
		Page<ResponseSearchProductDto> responsePage = productPages.map(productPage-> 
				ResponseSearchProductDto.builder()
				.name(productPage.getName())
				.price(productPage.getPrice())
				.description(productPage.getDescription())
				.ratings(productPage.getRatings())
				.seller(productPage.getSeller())
				.stock(productPage.getStock())
				.numOfReviews(productPage.getNumOfReviews())
				.imageUrl(productPage.getImageUrl())
				.category(productPage.getCategory())
				.brand(productPage.getBrand())
				.build()
			);
		return PaginationProductControllGetAllProductsResponseDto.<ResponseSearchProductDto>builder()
					.content(responsePage.getContent())
					.page(responsePage.getNumber())
					.size(responsePage.getSize())
					.totalPages(responsePage.getTotalPages())
					.totalElements(responsePage.getNumberOfElements())
					.last(responsePage.isLast())
					.build();
	}
}