package com.Cristofer.SoftComerce.DTO;

import java.time.LocalDateTime;

public class ShippingDTO {
    
    private int orderID;
    private String address;
    private String city;
    private String country;
    private String postalCode;
    private LocalDateTime createdAt;

    public ShippingDTO(int orderID, String address, String city, String country, String postalCode, LocalDateTime createdAt) {
        this.orderID = orderID;
        this.address = address;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.createdAt = createdAt;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}