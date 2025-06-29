package com.Cristofer.SoftComerce.DTO;

public class ResponseLogin {
    private String status;
    private String message;
    private String token;

    public ResponseLogin() {}

    public ResponseLogin(String status, String message, String token) {
        this.status = status;
        this.message = message;
        this.token = token;
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
}