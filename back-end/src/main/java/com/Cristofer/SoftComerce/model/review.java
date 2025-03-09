package com.Cristofer.SoftComerce.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "review")
public class review {

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

    // Relación con la entidad user (clave foránea)
    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private user user;

    // Relación con la entidad product (clave foránea)
    @ManyToOne
    @JoinColumn(name = "productID", nullable = false)
    private product product;

    // Constructores
    public review() {
    }

    public review(int reviewID, int rating, String comment, LocalDateTime createdAt, user user, product product) {
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

    public user getUser() {
        return user;
    }

    public void setUser(user user) {
        this.user = user;
    }

    public product getProduct() {
        return product;
    }

    public void setProduct(product product) {
        this.product = product;
    }
}