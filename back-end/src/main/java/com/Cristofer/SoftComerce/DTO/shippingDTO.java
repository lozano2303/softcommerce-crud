package com.Cristofer.SoftComerce.DTO;

import java.time.LocalDateTime;

public class shippingDTO {
    
    private int orderID;
    private String address;
    private String city;
    private String country;
    private String postal_code;
    private LocalDateTime created_at;

    public shippingDTO(int orderID, String address, String city, String country, String postal_code, LocalDateTime created_at) {
        this.orderID = orderID;
        this.address = address;
        this.city = city;
        this.country = country;
        this.postal_code = postal_code;
        this.created_at = created_at;
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

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
