package com.Cristofer.SoftComerce.DTO;

public class productcategoryDTO {

    private int productID;
    private int categoryID;

    // Constructor
    public productcategoryDTO(int productID, int categoryID) {
        this.productID = productID;
        this.categoryID = categoryID;
    }

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
}