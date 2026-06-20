package com.sdtechno.sdcart.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductControllerUpdateProductDto {

	private String name;
	private Double price;
	private String description;
	private Double ratings;
	private String seller;
	private Integer stock;
	private Integer noOfReviews;
	private String category;
	private String brand;
	private MultipartFile imageFile;
	
}
