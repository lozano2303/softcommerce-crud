package com.Cristofer.SoftComerce.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class PaymentOrderId implements Serializable {

    private int paymentID;
    private int orderID;

    // Getters y Setters
    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentOrderId that = (PaymentOrderId) o;
        return paymentID == that.paymentID && orderID == that.orderID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentID, orderID);
    }
}