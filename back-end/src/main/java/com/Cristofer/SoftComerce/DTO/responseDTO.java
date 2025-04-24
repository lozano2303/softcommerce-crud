package com.Cristofer.SoftComerce.DTO;

public class responseDTO {

    private String status;
    private String message;
    private String token; // Campo opcional para incluir un token

    // Constructor para respuestas sin token
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
}