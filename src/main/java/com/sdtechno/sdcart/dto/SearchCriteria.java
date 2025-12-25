package com.sdtechno.sdcart.dto;

import java.util.HashMap;
import java.util.Map;

public class SearchCriteria {

    private String keyword;      // redmi, rice, fan
    private String category;     // mobile, grocery, fruit, electrical
    private Double minPrice;
    private Double maxPrice;

    // Dynamic filters (ram, storage, weight, organic, etc.)
    private Map<String, String> attributes = new HashMap<>();

    // Getters & Setters
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

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
