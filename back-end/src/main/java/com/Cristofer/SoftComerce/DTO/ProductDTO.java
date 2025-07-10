package com.Cristofer.SoftComerce.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductDTO {
    private String name;
    private String description;
    private double price;
    private int stock;

    @JsonProperty("image_url")
    private String imageUrl;

    private int categoryID;

    public ProductDTO(String name, String description, double price, int stock, String imageUrl, int categoryID) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.categoryID = categoryID;
    }

    // Getters y Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }
}