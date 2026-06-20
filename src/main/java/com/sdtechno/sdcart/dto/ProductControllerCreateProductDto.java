package com.sdtechno.sdcart.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductControllerCreateProductDto {

	@NotBlank(message = "name must be provide")
	private String name;
	@NotBlank(message = "price must be provide")
	private double price;
	@NotBlank(message = "Description must be provide")
	private String description;
	private double ratings;
	@NotBlank(message = "seller must be provide")
	private String seller;
	@NotBlank(message = "No.of stock must be provide")
	private int stock;
	private int noOfReviews;
	@NotBlank(message = "Category must be provide")
	private String category;
	private String brand;
	@NotBlank(message = "Prduct Image must be provide")
	private MultipartFile imageFile;
}
