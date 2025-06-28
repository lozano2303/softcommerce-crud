package com.Cristofer.SoftComerce.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity(name = "orderproduct")
public class OrderProduct {

    @EmbeddedId
    private OrderProductId id; // Clave primaria compuesta

    @ManyToOne
    @MapsId("orderID") // Mapea la parte de orderID de la clave compuesta
    @JoinColumn(name = "orderID")
    private Order order;

    @ManyToOne
    @MapsId("productID") // Mapea la parte de productID de la clave compuesta
    @JoinColumn(name = "productID")
    private Product product;

    private int quantity;
    private double subtotal;

    // Getters y Setters
    public OrderProductId getId() {
        return id;
    }

    public void setId(OrderProductId id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
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