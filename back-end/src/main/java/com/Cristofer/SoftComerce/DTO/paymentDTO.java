package com.Cristofer.SoftComerce.DTO;

public class paymentDTO {

    private int userID;
    private double amount;
    private String method;

    public paymentDTO(int userID, double amount, String method) {
        this.userID = userID;
        this.amount = amount;
        this.method = method;
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

}
