package com.Cristofer.SoftComerce.DTO;

public class ReviewDTO {

    private int rating;
    private String comment;
    private int userID;    // Solo necesitamos el ID del usuario
    private int productID; // Solo necesitamos el ID del producto

    // Constructor
    public ReviewDTO(int rating, String comment, int userID, int productID) {
        this.rating = rating;
        this.comment = comment;
        this.userID = userID;
        this.productID = productID;
    }

    // Getters y Setters
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }
}