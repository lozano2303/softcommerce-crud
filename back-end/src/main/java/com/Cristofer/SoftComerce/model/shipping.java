package com.Cristofer.SoftComerce.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "shipping")
public class shipping {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shippingID")
    private int shippingID;

    @ManyToOne
    @JoinColumn(name = "orderID", nullable = false)
    private order order;

    @Column(name = "address", length = 255, nullable = false)
    private String address;

    @Column(name = "city", length = 100, nullable = false)
    private String city;

    @Column(name = "country", length = 100, nullable = false)
    private String country;

    @Column(name = "postal_code", length = 20, nullable = false)
    private String postal_code;

    @Column(name = "status", length = 50, nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime created_at;

    public shipping() {}

    public shipping(int shippingID, order order, String address, String city, String country, String postal_code, String status, LocalDateTime created_at) {
        this.shippingID = shippingID;
        this.order = order;
        this.address = address;
        this.city = city;
        this.country = country;
        this.postal_code = postal_code;
        this.status = status;
        this.created_at = created_at;
    }

    public int getShippingID() {
        return shippingID;
    }

    public void setShippingID(int shippingID) {
        this.shippingID = shippingID;
    }

    public order getOrder() {
        return order;
    }

    public void setOrder(order order) {
        this.order = order;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
