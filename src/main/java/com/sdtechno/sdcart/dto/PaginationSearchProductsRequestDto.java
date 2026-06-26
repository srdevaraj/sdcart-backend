package com.sdtechno.sdcart.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PaginationSearchProductsRequestDto {

	@Min(value = 0,message = "Pages starts with 0")
	private int page = 0;
	@Min(value = 25,message = "Size must contain 25 elements")
	@Max(value = 100,message = "Size not exceeds 100")
	private int size = 25;
	private String sortBy = "id";
	private String direction = "asc";

}
