package com.Cristofer.SoftComerce.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;


@Entity(name = "orderproduct")
public class orderproduct {

    @EmbeddedId
    private orderproductId id; // Clave primaria compuesta

    @ManyToOne
    @MapsId("orderID") // Mapea la parte de orderID de la clave compuesta
    @JoinColumn(name = "orderID")
    private order order;

    @ManyToOne
    @MapsId("productID") // Mapea la parte de productID de la clave compuesta
    @JoinColumn(name = "productID")
    private product product;

    private int quantity;
    private double subtotal;

    // Getters y Setters
    public orderproductId getId() {
        return id;
    }

    public void setId(orderproductId id) {
        this.id = id;
    }

    public order getOrder() {
        return order;
    }

    public void setOrder(order order) {
        this.order = order;
    }

    public product getProduct() {
        return product;
    }

    public void setProduct(product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}