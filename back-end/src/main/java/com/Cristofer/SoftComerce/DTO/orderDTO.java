package com.Cristofer.SoftComerce.DTO;

import java.time.LocalDateTime;

public class orderDTO {
    private int userID; // ID del usuario asociado a la orden
    private LocalDateTime createdAt; // Fecha de creación automática

    // Constructor que solo recibe el userID
    public orderDTO(int userID) {
        this.userID = userID;
        this.createdAt = LocalDateTime.now(); // Fecha de creación actual
    }

    // Getters y setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}