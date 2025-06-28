package com.Cristofer.SoftComerce.model;

import jakarta.persistence.*;

@Entity(name = "paymentorder")
public class PaymentOrder {

    @EmbeddedId
    private PaymentOrderId id; // Clave primaria compuesta

    @ManyToOne
    @MapsId("paymentID") // Mapea la parte de paymentID de la clave compuesta
    @JoinColumn(name = "paymentID")
    private Payment payment;

    @ManyToOne
    @MapsId("orderID") // Mapea la parte de orderID de la clave compuesta
    @JoinColumn(name = "orderID")
    private Order order;

    // Getters y Setters
    public PaymentOrderId getId() {
        return id;
    }

    public void setId(PaymentOrderId id) {
        this.id = id;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}