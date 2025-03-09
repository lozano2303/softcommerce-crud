package com.Cristofer.SoftComerce.DTO;

import java.time.LocalDateTime;

public class orderDTO {

    private int userID;
    private double totalPrice;
    private String status;
    private LocalDateTime createdAt;

    public orderDTO(int userID, double totalPrice, String status, LocalDateTime createdAt) {
        this.userID = userID;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
