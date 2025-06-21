package com.sdtechno.sdcart.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Transient;

import java.util.Base64;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;
    private String description;
    private double ratings;
    private String seller;
    private int stock;
    private int numOfReviews;

    @Lob
    private byte[] image;

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

    // Getters and setters
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

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    @Transient
    public String getImageBase64() {
        if (this.image != null) {
            return Base64.getEncoder().encodeToString(this.image);
        }
        return null;
    }
}
