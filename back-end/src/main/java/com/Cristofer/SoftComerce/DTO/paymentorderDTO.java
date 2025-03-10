package com.Cristofer.SoftComerce.DTO;

public class paymentorderDTO {

    private int paymentID;
    private int orderID;

    // Constructor
    public paymentorderDTO(int paymentID, int orderID) {
        this.paymentID = paymentID;
        this.orderID = orderID;
    }

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
}