package com.Cristofer.SoftComerce.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "product")

public class product{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productID")
    private int productID;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description", length = 150)
    private String description;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public product(int productID, String name, String description, double price, int stock, LocalDateTime createdAt) {
        this.productID = productID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.createdAt = createdAt;
    }

    public product() {
        // Inicializa los valores por defecto si es necesario
    }

    public int getproductID() {
        return productID;
    }

    public void setproductID(int productID) {
        this.productID = productID;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public double getprice() {
        return price;
    }

    public void setprice(double price) {
        this.price = price;
    }

    public int getstock() {
        return stock;
    }

    public void setstock(int stock) {
        this.stock = stock;
    }

    public LocalDateTime getcreatedAt() {
        return createdAt;
    }

    public void setcreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

