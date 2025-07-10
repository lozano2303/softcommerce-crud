package com.Cristofer.SoftComerce.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewID")
    private int reviewID;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "comment", length = 500, nullable = false)
    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Relación con la entidad User (clave foránea)
    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    // Relación con la entidad Product (clave foránea)
    @ManyToOne
    @JoinColumn(name = "productID", nullable = false)
    private Product product;

    // Constructores
    public Review() {
    }

    public Review(int reviewID, int rating, String comment, LocalDateTime createdAt, User user, Product product) {
        this.reviewID = reviewID;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.user = user;
        this.product = product;
    }

    // Getters y Setters
    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}