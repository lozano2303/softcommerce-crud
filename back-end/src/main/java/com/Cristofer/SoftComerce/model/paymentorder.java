package com.Cristofer.SoftComerce.model;

import jakarta.persistence.*;

@Entity(name = "paymentorder")
public class paymentorder {

    @EmbeddedId
    private paymentorderId id; // Clave primaria compuesta

    @ManyToOne
    @MapsId("paymentID") // Mapea la parte de paymentID de la clave compuesta
    @JoinColumn(name = "paymentID")
    private payment payment;

    @ManyToOne
    @MapsId("orderID") // Mapea la parte de orderID de la clave compuesta
    @JoinColumn(name = "orderID")
    private order order;

    // Getters y Setters
    public paymentorderId getId() {
        return id;
    }

    public void setId(paymentorderId id) {
        this.id = id;
    }

    public payment getPayment() {
        return payment;
    }

    public void setPayment(payment payment) {
        this.payment = payment;
    }

    public order getOrder() {
        return order;
    }

    public void setOrder(order order) {
        this.order = order;
    }
}