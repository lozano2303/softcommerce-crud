package com.Cristofer.SoftComerce.model;

import jakarta.persistence.*;

@Entity(name = "productcategory")
public class productcategory {

    @EmbeddedId
    private productcategoryId id; // Clave primaria compuesta

    @ManyToOne
    @MapsId("productID") // Mapea la parte de productID de la clave compuesta
    @JoinColumn(name = "productID")
    private product product;

    @ManyToOne
    @MapsId("categoryID") // Mapea la parte de categoryID de la clave compuesta
    @JoinColumn(name = "categoryID")
    private category category;

    // Getters y Setters
    public productcategoryId getId() {
        return id;
    }

    public void setId(productcategoryId id) {
        this.id = id;
    }

    public product getProduct() {
        return product;
    }

    public void setProduct(product product) {
        this.product = product;
    }

    public category getCategory() {
        return category;
    }

    public void setCategory(category category) {
        this.category = category;
    }
}