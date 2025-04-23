package com.Cristofer.SoftComerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "category")
public class category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryID")
    private int categoryID;

    @Column(name = "categoryName", length = 50, nullable = false)
    private String categoryName;

    public category() {}

    public category(int categoryID, String categoryName) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
    }

    // Getter para categoryID
    public int getcategoryID() {
        return categoryID;
    }

    // Setter para categoryID
    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    // Getter para categoryName
    public String getCategoryName() {
        return categoryName;
    }

    // Setter para categoryName
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}