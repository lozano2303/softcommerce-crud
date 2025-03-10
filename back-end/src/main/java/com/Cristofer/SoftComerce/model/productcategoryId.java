package com.Cristofer.SoftComerce.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class productcategoryId implements Serializable {

    private int productID;
    private int categoryID;

    // Getters y Setters
    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        productcategoryId that = (productcategoryId) o;
        return productID == that.productID && categoryID == that.categoryID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productID, categoryID);
    }
}