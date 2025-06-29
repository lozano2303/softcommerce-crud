package com.Cristofer.SoftComerce.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "recoveryrequest")
public class RecoveryRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recoveryRequestID")
    private int recoveryRequestID;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User userID;

    @Column(name = "token", length = 255, nullable = false)
    private String token;

    @Column(name = "isUsed", nullable = false)
    private boolean isUsed;

    @Column(name = "expiresAt", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    // Constructor sin parámetros
    public RecoveryRequest() {
    }

    // Constructor con todos los campos
    public RecoveryRequest(int recoveryRequestID, User userID, String token, boolean isUsed, LocalDateTime expiresAt, LocalDateTime createdAt) {
        this.recoveryRequestID = recoveryRequestID;
        this.userID = userID;
        this.token = token;
        this.isUsed = isUsed;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    // Getters y setters
    public int getRecoveryRequestID() {
        return recoveryRequestID;
    }

    public void setRecoveryRequestID(int recoveryRequestID) {
        this.recoveryRequestID = recoveryRequestID;
    }

    public User getUserID() {
        return userID;
    }

    public void setUserID(User userID) {
        this.userID = userID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}