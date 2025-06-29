package com.sdtechno.sdcart.models;

import jakarta.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;
    private String description;
    private double ratings;
    private String seller;
    private int stock;
    private int numOfReviews;

    // ✅ Store only image URL (file saved to uploads/)
    private String imageUrl;

    public Product() {
        super();
    }

    public Product(String name, double price, String description, double ratings, String seller, int stock, int numOfReviews) {
        super();
        this.name = name;
        this.price = price;
        this.description = description;
        this.ratings = ratings;
        this.seller = seller;
        this.stock = stock;
        this.numOfReviews = numOfReviews;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getRatings() { return ratings; }
    public void setRatings(double ratings) { this.ratings = ratings; }

    public String getSeller() { return seller; }
    public void setSeller(String seller) { this.seller = seller; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getNumOfReviews() { return numOfReviews; }
    public void setNumOfReviews(int numOfReviews) { this.numOfReviews = numOfReviews; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
