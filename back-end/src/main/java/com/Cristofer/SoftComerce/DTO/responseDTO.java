package com.Cristofer.SoftComerce.DTO;

import com.Cristofer.SoftComerce.model.User;

public class ResponseDTO {

    private String status; // Estado de la respuesta (success o error)
    private String message; // Mensaje descriptivo
    private String token; // Campo opcional para incluir un token
    private User user; // Campo opcional para información del usuario

    // Constructor para respuestas sin token ni usuario
    public ResponseDTO(String status, String message) {
        this.status = status;
        this.message = message;
    }

    // Constructor para respuestas con token
    public ResponseDTO(String status, String message, String token) {
        this.status = status;
        this.message = message;
        this.token = token;
    }

    // Constructor para respuestas con token y usuario
    public ResponseDTO(String status, String message, String token, User user) {
        this.status = status;
        this.message = message;
        this.token = token;
        this.user = user;
    }

    // Getters y setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}