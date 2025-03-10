package com.Cristofer.SoftComerce.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class orderproductId implements Serializable {

    private int orderID;
    private int productID;

    // Getters y Setters
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        orderproductId that = (orderproductId) o;
        return orderID == that.orderID && productID == that.productID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderID, productID);
    }
}