package com.Cristofer.SoftComerce.DTO;

import com.Cristofer.SoftComerce.model.user;

public class responseDTO {

    private String status; // Estado de la respuesta (success o error)
    private String message; // Mensaje descriptivo
    private String token; // Campo opcional para incluir un token
    private user user; // Campo opcional para información del usuario

    // Constructor para respuestas sin token ni usuario
    public responseDTO(String status, String message) {
        this.status = status;
        this.message = message;
    }

    // Constructor para respuestas con token
    public responseDTO(String status, String message, String token) {
        this.status = status;
        this.message = message;
        this.token = token;
    }

    // Constructor para respuestas con token y usuario
    public responseDTO(String status, String message, String token, user user) {
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

    public user getUser() {
        return user;
    }

    public void setUser(user user) {
        this.user = user;
    }
}