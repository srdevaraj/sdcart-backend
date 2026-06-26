package com.sdtechno.sdcart.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationProductControllGetAllProductsResponseDto<T> {

	private List<T> content;
	private int page;
	private int size;
	private int totalPages;
	private int totalElements;
	private boolean last;
	
}
