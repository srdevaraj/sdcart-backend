package com.sdtechno.sdcart.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductControllerGetAllProductsResponseDto {

	private Long id;
	private String name;
    private double price;
    private String imageUrl;

}
