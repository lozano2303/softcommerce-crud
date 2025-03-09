package com.Cristofer.SoftComerce.DTO;

public class paymentDTO {

    private int userID;
    private double amount;
    private String method;
    private String status;

    public paymentDTO(int userID, double amount, String method, String status) {
        this.userID = userID;
        this.amount = amount;
        this.method = method;
        this.status = status;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
