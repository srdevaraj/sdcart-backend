package com.sdtechno.sdcart.dto;

import java.util.HashMap;
import java.util.Map;

public class SearchCriteria {

    private String keyword;     // redmi, rice, fan
    private String category;    // mobile, grocery
    private String brand;       // redmi, samsung
    private Double minPrice;
    private Double maxPrice;

    private Map<String, String> attributes = new HashMap<>();

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }
}
