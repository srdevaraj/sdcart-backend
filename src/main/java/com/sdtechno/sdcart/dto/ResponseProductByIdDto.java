package com.sdtechno.sdcart.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseProductByIdDto {

    private String name;
    private double price;
    private String description;
    private double ratings;
    private String seller;
    private int stock;
    private int numOfReviews;
    private String imageUrl;
    private String category;
    private String brand;
    
}
