package com.Cristofer.SoftComerce.DTO;

import java.time.LocalDateTime;

public class orderDTO {
    private int userID; // Cambiar el tipo a int (o String si el ID es String)
    private double totalPrice;
    private LocalDateTime createdAt;

    public orderDTO(int userID, double totalPrice, LocalDateTime createdAt) {
        this.userID = userID;
        this.totalPrice = totalPrice;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}